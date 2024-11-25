package com.deni.mallcoursework.controller;

import com.deni.mallcoursework.domain.product.dto.CreateProductDto;
import com.deni.mallcoursework.domain.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/create")
    public String upload(CreateProductDto createProductDto) {
        return "products/create";
    }

    @PostMapping("/create")
    public String create(@Valid CreateProductDto createProductDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("createProductDto", createProductDto);
            return "products/create";
        }

        productService.create(createProductDto);
        return "redirect:/";
    }
}
