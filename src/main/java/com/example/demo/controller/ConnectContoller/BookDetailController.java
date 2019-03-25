package com.example.demo.controller.ConnectContoller;

import com.example.demo.libEntity.Detail;
import com.example.demo.service.ConnectService.BookDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.UnsupportedEncodingException;

@RestController
public class BookDetailController {

    @Autowired
    private BookDetailService bookDetailService;

    @RequestMapping("bookDetail")
    public Detail[] getBookDetail(String search_book)
            throws UnsupportedEncodingException {
        return bookDetailService.getBookDetail(search_book);
    }
}
