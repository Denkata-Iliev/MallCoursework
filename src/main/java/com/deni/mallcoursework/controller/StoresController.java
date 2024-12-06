package com.deni.mallcoursework.controller;

import com.deni.mallcoursework.domain.account.service.UserService;
import com.deni.mallcoursework.domain.store.dto.CreateStoreDto;
import com.deni.mallcoursework.domain.store.service.StoreService;
import com.deni.mallcoursework.infrastructure.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/stores")
public class StoresController {
    private final UserService userService;
    private final StoreService storeService;

    public StoresController(UserService userService, StoreService storeService) {
        this.userService = userService;
        this.storeService = storeService;
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
    public String getById(@PathVariable String id, RedirectAttributes redirectAttributes, Model model) {
        try {
            var store = storeService.getById(id);
            model.addAttribute("store", store);

            return "stores/details";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/stores";
        }
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("createStoreDto", new CreateStoreDto());
        model.addAttribute("managers", userService.getAllManagers(null));
        return "stores/create";
    }

    @PostMapping("/create")
    public String create(@Valid CreateStoreDto createStoreDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("createStoreDto", createStoreDto);
            return "stores/create";
        }

        storeService.create(createStoreDto);
        return "redirect:/stores";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            var createStoreDto = storeService.getCreateDtoById(id);
            model.addAttribute("createStoreDto", createStoreDto);
            model.addAttribute("managers", userService.getAllManagers(createStoreDto.getManagerId()));

            return "stores/update";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/stores";
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable String id,
                         @Valid CreateStoreDto createStoreDto,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("createStoreDto", createStoreDto);
            return "stores/update";
        }

        try {
            storeService.update(createStoreDto, id);
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/stores";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            storeService.delete(id);
            return "redirect:/stores";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/stores";
        }
    }
}
