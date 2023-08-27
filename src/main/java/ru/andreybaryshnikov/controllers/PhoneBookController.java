package ru.andreybaryshnikov.controllers;

import com.sun.net.httpserver.HttpContext;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.andreybaryshnikov.models.PhoneBook;
import ru.andreybaryshnikov.services.PhoneBookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PhoneBookController {
    private PhoneBookService phoneBookService;

    public PhoneBookController(PhoneBookService phoneBookService) {
        this.phoneBookService = phoneBookService;
    }

    @GetMapping(value = "/")
    public String showFirstView(@AuthenticationPrincipal User user, Model model) {
        Iterable<PhoneBook> phoneBooks =  phoneBookService.getPhoneBooks();
        if ( user == null) {
            model.addAttribute("typeSign",0);
        } else {
            String userName = user.getUsername();
            model.addAttribute("typeSign",1);
            model.addAttribute("userName", userName);
        }
        model.addAttribute("phoneBooks",phoneBooks);
        return "PhoneBook/index";
    }

    @GetMapping(value = "/viewRecord")
    public String viewGetRecord(@RequestParam("id") int id, Model model) {
        model.addAttribute("phoneBook", phoneBookService.getPhoneBook(id));
        return "PhoneBook/view-record";
    }

    @PostMapping(value = "/deleteRecord")
    public String deleteRecord(@RequestParam("id") int id, Model model) {
        phoneBookService.deleteRecordToPhoneBooks(id);
        return "redirect:/";
    }

    @PostMapping(value = "/editRecord")
    public String editRecord(@RequestParam("id") int id, Model model) {
        model.addAttribute("phoneBook", phoneBookService.getPhoneBook(id));
        return "PhoneBook/edit-record";
    }

    @PostMapping(value = "/editSaveRecord")
    public String editSaveRecord(@ModelAttribute("phoneBook") PhoneBook phoneBook, Model model) {
        phoneBookService.editRecordToPhoneBooks(phoneBook);
        return "redirect:/";
    }
    @PostMapping("/viewAddRecord")
    public String viewAddRecord(Model model) {
        model.addAttribute("phoneBook", phoneBookService.getNewPhoneBook());
        return "PhoneBook/view-add-record";
    }
    @PostMapping(value = "/addSaveRecord")
    public String addSaveRecord(@ModelAttribute("phoneBook") PhoneBook phoneBook, Model model) {
        phoneBookService.addRecordToPhoneBooks(phoneBook);
        return "redirect:/";
    }
}
