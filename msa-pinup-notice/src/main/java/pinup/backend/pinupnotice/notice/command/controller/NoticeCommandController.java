package pinup.backend.pinupnotice.notice.command.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pinup.backend.pinupnotice.notice.command.dto.NoticePostRequest;
import pinup.backend.pinupnotice.notice.command.service.NoticeCommandService;

@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticeCommandController {

    private final NoticeCommandService noticeCommandService;

    @PostMapping
    public ResponseEntity<Long> postNotice(@RequestBody NoticePostRequest request) {
        return ResponseEntity.ok(noticeCommandService.postNotice(request));
    }
}
