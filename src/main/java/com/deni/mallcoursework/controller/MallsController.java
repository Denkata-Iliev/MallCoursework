package com.deni.mallcoursework.controller;

import com.deni.mallcoursework.domain.mall.dto.CreateMallDto;
import com.deni.mallcoursework.domain.mall.dto.UpdateMallDto;
import com.deni.mallcoursework.domain.mall.service.MallService;
import com.deni.mallcoursework.domain.store.entity.Store;
import com.deni.mallcoursework.domain.store.service.StoreService;
import com.deni.mallcoursework.domain.user.service.UserService;
import com.deni.mallcoursework.infrastructure.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/malls")
public class MallsController {

    private final MallService mallService;
    private final UserService userService;
    private final StoreService storeService;

    @Autowired
    public MallsController(MallService mallService, UserService userService, StoreService storeService) {
        this.mallService = mallService;
        this.userService = userService;
        this.storeService = storeService;
    }

    @PreAuthorize("hasRole('ROLE_MALL_OWNER')")
    @GetMapping("/my-malls")
    public String myMalls(@RequestParam(name = "page", defaultValue = "0") int pageNum,
                          @RequestParam(defaultValue = "5") int size,
                          Model model,
                          Authentication authentication) {
        Pageable pageable = PageRequest.of(pageNum, size);
        var mallsPage = mallService.getMallsOfCurrentUser(authentication, pageable);

        model.addAttribute("malls", mallsPage.getContent());
        model.addAttribute("page", mallsPage);

        return "malls/my-malls";
    }

    @GetMapping
    public String index(@RequestParam(name = "page", defaultValue = "0") int pageNum,
                        @RequestParam(defaultValue = "5") int size,
                        Model model) {
        Pageable pageable = PageRequest.of(pageNum, size);
        var mallsPage = mallService.getAll(pageable);

        model.addAttribute("malls", mallsPage.getContent());
        model.addAttribute("page", mallsPage);

        return "malls/index";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/create")
    public String create(CreateMallDto createMallDto, Model model) {
        model.addAttribute("mallOwners", userService.getAllMallOwners());
        return "malls/create";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public String create(@Valid CreateMallDto createMallDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("createMallDto", createMallDto);
            model.addAttribute("mallOwners", userService.getAllMallOwners());
            return "malls/create";
        }

        mallService.createMall(createMallDto);
        return "redirect:/malls";
    }

    @GetMapping("/{id}")
    public String stores(@PathVariable String id,
                         @RequestParam(name = "page", defaultValue = "0") int pageNum,
                         @RequestParam(defaultValue = "5") int size,
                         Model model,
                         Authentication authentication) {
        Pageable pageable = PageRequest.of(pageNum, size);
        var page = storeService.getAll(pageable, id);

        if (authentication != null) {
            var user = userService.getCurrentUserEntity(authentication);
            var userFavorites = user.getFavorites()
                    .stream()
                    .map(Store::getId)
                    .toList();

            model.addAttribute("userFavorites", userFavorites);
        }

        model.addAttribute("stores", page.getContent());
        model.addAttribute("page", page);

        return "malls/stores";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable String id, Model model) {
        try {
            model.addAttribute("mall", mallService.getDetailsById(id));
            return "malls/details";
        } catch (ResourceNotFoundException e) {
            return "redirect:/error/404";
        }
    }

    @PreAuthorize("@mallExpression.isAllowedToModifyMall(#id)")
    @GetMapping("/update/{id}")
    public String update(@PathVariable String id, Model model) {
        try {
            var updateMallDto = mallService.getUpdateDtoById(id);
            var mallOwners = userService.getAllMallOwners();

            model.addAttribute("updateMallDto", updateMallDto);
            model.addAttribute("mallOwners", mallOwners);

            return "malls/update";
        } catch (ResourceNotFoundException e) {
            return "redirect:/error/404";
        }
    }

    @PreAuthorize("@mallExpression.isAllowedToModifyMall(#id)")
    @PostMapping("/update/{id}")
    public String update(@PathVariable String id,
                         @Valid UpdateMallDto updateMallDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("updateMallDto", updateMallDto);
            return "malls/update";
        }

        try {
            mallService.update(updateMallDto, id);

            return "redirect:/malls";
        } catch (ResourceNotFoundException e) {
            return "redirect:/error/404";
        }
    }

    @PreAuthorize("@mallExpression.isAllowedToModifyMall(#id)")
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        try {
            mallService.delete(id);

            return "redirect:/malls";
        } catch (ResourceNotFoundException e) {
            return "redirect:/error/404";
        }
    }
}
