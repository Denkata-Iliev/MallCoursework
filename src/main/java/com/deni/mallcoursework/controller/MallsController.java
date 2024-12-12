package com.deni.mallcoursework.controller;

import com.deni.mallcoursework.domain.mall.dto.CreateMallDto;
import com.deni.mallcoursework.domain.mall.service.MallService;
import com.deni.mallcoursework.domain.user.service.UserService;
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
}
