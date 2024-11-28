package com.deni.mallcoursework.controller;

import com.deni.mallcoursework.domain.product.dto.CreateProductDto;
import com.deni.mallcoursework.domain.product.dto.DisplayProductDto;
import com.deni.mallcoursework.domain.product.service.ProductService;
import com.deni.mallcoursework.infrastructure.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductsController {
    private final ProductService productService;

    @Autowired
    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String index(@RequestParam(name = "page", defaultValue = "0") int pageNum,
                        @RequestParam(defaultValue = "9") int size,
                        Model model) {
        Pageable pageable = PageRequest.of(pageNum, size);
        var page = productService.getAll(pageable);

        model.addAttribute("products", page.getContent());
        model.addAttribute("page", page);

        return "products/index";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable String id, RedirectAttributes redirectAttributes, Model model) {
        DisplayProductDto productDto;

        try {
            productDto = productService.getById(id);
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/products";
        }

        model.addAttribute("product", productDto);
        return "products/details";
    }

    @GetMapping("/create")
    public String create(CreateProductDto createProductDto) {
        return "products/create";
    }

    @PostMapping("/create")
    public String create(@Valid CreateProductDto createProductDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("createProductDto", createProductDto);
            return "products/create";
        }

        productService.create(createProductDto);
        return "redirect:/products";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable String id, Model model) {
        var createProductDto = productService.getCreateDtoById(id);
        model.addAttribute("createProductDto", createProductDto);
        return "products/update";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable String id,
                         @Valid CreateProductDto createProductDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("createProductDto", createProductDto);
            return "products/update";
        }

        try {
            productService.update(createProductDto, id);
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/product/update/" + id;
        }

        return "redirect:/products";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id, Model model) {
        try {
            productService.delete(id);
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "redirect:/products";
    }
}
