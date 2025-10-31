package pinup.backend.notice.query.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pinup.backend.notice.query.dto.NoticeListResponse;
import pinup.backend.notice.query.dto.NoticeSpecificResponse;
import pinup.backend.notice.query.service.NoticeQueryService;

import java.util.List;

@RestController("/notices")
@RequiredArgsConstructor
public class NoticeQueryController {

    private final NoticeQueryService noticeQueryService;

    @GetMapping
    public ResponseEntity<List<NoticeListResponse>> getAllNotices() {
        return ResponseEntity.ok(noticeQueryService.getAllNotices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeSpecificResponse> getSpecificNotice(@PathVariable Integer id) {
        return ResponseEntity.ok(noticeQueryService.getNoticeById(id));
    }
}
