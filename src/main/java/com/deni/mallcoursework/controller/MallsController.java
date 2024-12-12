package com.deni.mallcoursework.controller;

import com.deni.mallcoursework.domain.mall.dto.CreateMallDto;
import com.deni.mallcoursework.domain.mall.service.MallService;
import com.deni.mallcoursework.domain.user.service.UserService;
import com.deni.mallcoursework.infrastructure.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/malls")
public class MallsController {

    private final MallService mallService;
    private final UserService userService;

    @Autowired
    public MallsController(MallService mallService, UserService userService) {
        this.mallService = mallService;
        this.userService = userService;
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

    @GetMapping("/create")
    public String create(CreateMallDto createMallDto, Model model) {
        model.addAttribute("mallOwners", userService.getAllMallOwners());
        return "malls/create";
    }

    @PostMapping("/create")
    public String create(@Valid CreateMallDto createMallDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("createMallDto", createMallDto);
            return "malls/create";
        }

        mallService.createMall(createMallDto);
        return "redirect:/malls";
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

    @GetMapping("/update/{id}")
    public String update(@PathVariable String id, Model model) {
        try {
            var createMallDto = mallService.getCreateDtoById(id);
            var mallOwners = userService.getAllMallOwners();

            model.addAttribute("createMallDto", createMallDto);
            model.addAttribute("mallOwners", mallOwners);

            return "malls/update";
        } catch (ResourceNotFoundException e) {
            return "redirect:/error/404";
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable String id,
                         @Valid CreateMallDto createMallDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("createMallDto", createMallDto);
            return "malls/update";
        }

        try {
            mallService.update(createMallDto, id);

            return "redirect:/malls";
        } catch (ResourceNotFoundException e) {
            return "redirect:/error/404";
        }
    }
}
