package pinup.backend.notice.command.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pinup.backend.notice.command.dto.NoticePostRequest;
import pinup.backend.notice.command.service.NoticeCommandService;

@RestController("/noticess")
@RequiredArgsConstructor
public class NoticeCommandController {

    private final NoticeCommandService noticeCommandService;

    @PostMapping
    public ResponseEntity<Integer> postNotice(@RequestBody NoticePostRequest request) {
        return ResponseEntity.ok(noticeCommandService.postNotice(request));
    }
}
