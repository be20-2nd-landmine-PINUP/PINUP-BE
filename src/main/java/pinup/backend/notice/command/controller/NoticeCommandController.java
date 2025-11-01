package pinup.backend.notice.command.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pinup.backend.notice.command.dto.NoticePatchRequest;
import pinup.backend.notice.command.dto.NoticePatchResponse;
import pinup.backend.notice.command.dto.NoticePostRequest;
import pinup.backend.notice.command.service.NoticeCommandService;

@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticeCommandController {

    private final NoticeCommandService noticeCommandService;

    @PostMapping
    public ResponseEntity<Integer> postNotice(@RequestBody NoticePostRequest request) {
        return ResponseEntity.ok(noticeCommandService.postNotice(request));
    }

    @PatchMapping
    public ResponseEntity<NoticePatchResponse> patchNotice(@RequestBody NoticePatchRequest request) {
        return ResponseEntity.ok(noticeCommandService.patchNotice(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteNotice(@PathVariable Integer id) {
        return ResponseEntity.ok(noticeCommandService.deleteNotice(id));
    }
}
