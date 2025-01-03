package com.deni.mallcoursework.controller;

import com.deni.mallcoursework.domain.product.service.ProductService;
import com.deni.mallcoursework.domain.store.dto.CreateStoreDto;
import com.deni.mallcoursework.domain.store.service.StoreService;
import com.deni.mallcoursework.domain.user.service.UserService;
import com.deni.mallcoursework.infrastructure.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    @PreAuthorize("@storeExpression.worksAtStore()")
    @GetMapping("/my-store")
    public String myStore(Model model, Authentication authentication) {
        var store = storeService.getStoreOfCurrentUser(authentication);
        model.addAttribute("store", store);

        return "stores/my-store";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable String id,
                          @RequestParam(name = "page", defaultValue = "0") int pageNum,
                          @RequestParam(defaultValue = "9") int size,
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

    @PreAuthorize("@storeExpression.isAllowedToCreateEmployee(#id)")
    @GetMapping("/{id}/employees")
    public String employees(@PathVariable String id,
                            @RequestParam(name = "page", defaultValue = "0") int pageNum,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {
        try {
            Pageable pageable = PageRequest.of(pageNum, size);
            var employees = storeService.getEmployeesById(pageable, id);
            model.addAttribute("employees", employees.getContent());
            model.addAttribute("page", employees);

            return "stores/employees";
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

    @PreAuthorize("@storeExpression.isAllowedToCreateStore(#mallId)")
    @GetMapping("/create/{mallId}")
    public String create(@PathVariable String mallId, CreateStoreDto createStoreDto, Model model) {
        model.addAttribute("managers", userService.getAllManagers(null));
        return "stores/create";
    }

    @PreAuthorize("@storeExpression.isAllowedToCreateStore(#mallId)")
    @PostMapping("/create/{mallId}")
    public String create(@PathVariable String mallId,
                         @Valid CreateStoreDto createStoreDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("createStoreDto", createStoreDto);
            model.addAttribute("managers", userService.getAllManagers(null));
            return "stores/create";
        }

        storeService.create(createStoreDto, mallId);
        return "redirect:/malls/" + mallId;
    }

    @PreAuthorize("@storeExpression.isAllowedToUpdateStore(#id) || @storeExpression.isAllowedToDeleteOrChangeManagerOfStore(null, #id)")
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

    @PreAuthorize("@storeExpression.isAllowedToUpdateStore(#id) || @storeExpression.isAllowedToDeleteOrChangeManagerOfStore(null, #id)")
    @PostMapping("/update/{id}")
    public String update(@PathVariable String id,
                         @Valid CreateStoreDto createStoreDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("createStoreDto", createStoreDto);
            model.addAttribute("managers", userService.getAllManagers(createStoreDto.getManagerId()));

            return "stores/update";
        }

        try {
            var mallId = storeService.update(createStoreDto, id);
            return "redirect:/malls/" + mallId;
        } catch (ResourceNotFoundException e) {
            return "redirect:/error/404";
        }
    }

    @PreAuthorize("@storeExpression.isAllowedToDeleteOrChangeManagerOfStore(null, #id)")
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        try {
            var mallId = storeService.delete(id);
            return "redirect:/malls/" + mallId;
        } catch (ResourceNotFoundException e) {
            return "redirect:/error/404";
        }
    }
}
