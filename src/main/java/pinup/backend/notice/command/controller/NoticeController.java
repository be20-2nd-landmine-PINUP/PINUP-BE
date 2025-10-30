package pinup.backend.notice.command.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import pinup.backend.notice.command.service.NoticeService;

@RestController("/notices")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;
}
