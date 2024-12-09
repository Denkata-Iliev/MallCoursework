package com.deni.mallcoursework.controller;

import com.deni.mallcoursework.domain.account.service.UserService;
import com.deni.mallcoursework.domain.product.service.ProductService;
import com.deni.mallcoursework.domain.store.dto.CreateStoreDto;
import com.deni.mallcoursework.domain.store.service.StoreService;
import com.deni.mallcoursework.infrastructure.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/stores")
public class StoresController {
    private final UserService userService;
    private final StoreService storeService;
    private final ProductService productService;

    public StoresController(UserService userService, StoreService storeService, ProductService productService) {
        this.userService = userService;
        this.storeService = storeService;
        this.productService = productService;
    }

    @GetMapping
    public String index(@RequestParam(name = "page", defaultValue = "0") int pageNum,
                        @RequestParam(defaultValue = "5") int size,
                        Model model) {
        Pageable pageable = PageRequest.of(pageNum, size);
        var page = storeService.getAll(pageable);

        model.addAttribute("stores", page.getContent());
        model.addAttribute("page", page);

        return "stores/index";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable String id,
                          @RequestParam(name = "page", defaultValue = "0") int pageNum,
                          @RequestParam(defaultValue = "5") int size,
                          Model model) {
        try {
            Pageable pageable = PageRequest.of(pageNum, size);
            var productsPage = productService.getAll(pageable, id);
            model.addAttribute("products", productsPage.getContent());
            model.addAttribute("page", productsPage);

            return "stores/products";
        } catch (ResourceNotFoundException e) {
            return "redirect:/error/404";
        }
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable String id, Model model) {
        try {
            var detailsDto = storeService.getDetailsById(id);

            model.addAttribute("store", detailsDto);
            return "stores/details";
        } catch (ResourceNotFoundException e) {
            return "redirect:/error/404";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MALL_OWNER')")
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("createStoreDto", new CreateStoreDto());
        model.addAttribute("managers", userService.getAllManagers(null));
        return "stores/create";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MALL_OWNER')")
    @PostMapping("/create")
    public String create(@Valid CreateStoreDto createStoreDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("createStoreDto", createStoreDto);
            return "stores/create";
        }

        storeService.create(createStoreDto);
        return "redirect:/stores";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MALL_OWNER', 'ROLE_MANAGER')")
    @GetMapping("/update/{id}")
    public String update(@PathVariable String id, Model model) {
        try {
            var createStoreDto = storeService.getCreateDtoById(id);
            model.addAttribute("createStoreDto", createStoreDto);
            model.addAttribute("managers", userService.getAllManagers(createStoreDto.getManagerId()));

            return "stores/update";
        } catch (ResourceNotFoundException e) {
            return "redirect:/error/404";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MALL_OWNER', 'ROLE_MANAGER')")
    @PostMapping("/update/{id}")
    public String update(@PathVariable String id,
                         @Valid CreateStoreDto createStoreDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("createStoreDto", createStoreDto);
            return "stores/update";
        }

        try {
            storeService.update(createStoreDto, id);
        } catch (ResourceNotFoundException e) {
            return "redirect:/error/404";
        }

        return "redirect:/stores";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MALL_OWNER')")
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        try {
            storeService.delete(id);
            return "redirect:/stores";
        } catch (ResourceNotFoundException e) {
            return "redirect:/error/404";
        }
    }
}
