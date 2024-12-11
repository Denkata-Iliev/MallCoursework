package com.deni.mallcoursework.controller;

import com.deni.mallcoursework.domain.product.dto.CreateProductDto;
import com.deni.mallcoursework.domain.product.service.ProductService;
import com.deni.mallcoursework.infrastructure.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductsController {
    private final ProductService productService;

    @Autowired
    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable String id, Model model) {
        try {
            var displayProductDto = productService.getById(id);

            model.addAttribute("product", displayProductDto);
            return "products/details";
        } catch (ResourceNotFoundException e) {
            return "redirect:/error/404";
        }
    }

    @PreAuthorize("@authorizationService.isAllowedToModifyProductsWithStoreId(#storeId)")
    @GetMapping("/create/{storeId}")
    public String create(@PathVariable String storeId, CreateProductDto createProductDto) {
        return "products/create";
    }

    @PreAuthorize("@authorizationService.isAllowedToModifyProductsWithStoreId(#storeId)")
    @PostMapping("/create/{storeId}")
    public String create(@PathVariable String storeId,
                         @Valid CreateProductDto createProductDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("createProductDto", createProductDto);
            return "products/create";
        }

        productService.create(createProductDto, storeId);
        return "redirect:/stores/" + storeId;
    }

    @PreAuthorize("@authorizationService.isAllowedToModifyProductsWithId(#id)")
    @GetMapping("/update/{id}")
    public String update(@PathVariable String id, Model model) {
        try {
            var createProductDto = productService.getCreateDtoById(id);
            model.addAttribute("createProductDto", createProductDto);

            return "products/update";
        } catch (ResourceNotFoundException e) {
            return "redirect:/error/404";
        }
    }

    @PreAuthorize("@authorizationService.isAllowedToModifyProductsWithId(#id)")
    @PostMapping("/update/{id}")
    public String update(@PathVariable String id,
                         @Valid CreateProductDto createProductDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("createProductDto", createProductDto);
            return "products/update";
        }

        String storeId;
        try {
            storeId = productService.update(createProductDto, id);
        } catch (ResourceNotFoundException e) {
            return "redirect:/error/404";
        }

        return "redirect:/stores/" + storeId;
    }

    @PreAuthorize("@authorizationService.isAllowedToModifyProductsWithId(#id)")
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        String storeId;
        try {
            storeId = productService.delete(id);
        } catch (RuntimeException e) {
            return "redirect:/error/404";
        }

        return "redirect:/stores/" + storeId;
    }
}
