package kr.or.sspl.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.sspl.dto.QnaDto;
import kr.or.sspl.service.QnaService;

@Controller
@RequestMapping("/qna/")
public class QnAController {
	@Autowired
	private QnaService qnaService;

	// 리스트 페이지 이동
	@GetMapping("qnaList.do")
	public String qnaList(Model model, HttpServletRequest request) {
		qnaService.qnaList(model, request);
		return "qna/qna_list";
	}
	
	// 상세 페이지 이동
	@GetMapping("qnaDetail.do")
	public String qnaDetail(Model model, String qna_seq) {
		qnaService.qnaDetail(model, qna_seq);
		return "qna/qna_detail";
	}
	
	// 수정 페이지 이동
	@GetMapping("qnaModify.do")
	public String qnaModify() {
		return "qna/qna_modify";
	}
	
	// 글쓰기 페이지 이동
	@GetMapping("qnaWrite.do")
	public String qnaWrite() {
		return "qna/qna_write";
	}
	
	// 글쓰기
	@RequestMapping("/qnaWriteOk.do")
	public String qnaWriteOk(QnaDto qnaDto, HttpServletRequest request, HttpServletResponse response) {
		qnaService.qnaWriteOk(qnaDto, request, response);
		return "redirect:/qna/qnaList.do";		
	}
	
	// 글삭제
	@RequestMapping("/delete.do")
	public String delete(String qna_seq) {
		qnaService.qnadelete(qna_seq);
		return "redirect:/qna/qnaList.do";
	}
	
	// 댓글 작성
	@RequestMapping("/qnaReplyOk.do")
	public String qnaReplyOk(String user_id, String qna_seq, String qna_reply_content) {
		System.out.println(qna_seq + " " + qna_reply_content);
		System.out.println(user_id);
		qnaService.qnaReply(user_id, qna_seq, qna_reply_content);
		return "redirect:/qna/qnaDetail.do?qna_seq="+qna_seq;
	}
}
