package kr.or.sspl.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import kr.or.sspl.dao.QnaDao;
import kr.or.sspl.dto.QnaDto;
import kr.or.sspl.dto.QnaReplyDto;

@Service
public class QnaService {
	private SqlSession sqlsession;

	@Autowired
	public void setSqlsession(SqlSession sqlsession) {
		this.sqlsession = sqlsession;
	}
	
	// 글쓰기
	public void qnaWriteOk(QnaDto qnaDto, HttpServletRequest request, HttpServletResponse response) {
		try {
			System.out.println(qnaDto.getUser_id() + " " + qnaDto.getQna_title() + " " + qnaDto.getQna_content() + " " + qnaDto.getQna_category());
			QnaDao qnaDao = sqlsession.getMapper(QnaDao.class);
			qnaDao.qnaWrite(qnaDto);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 글출력
	public void qnaList(Model model, HttpServletRequest request) {
		List<QnaDto> qnaList = null;
		Map<String, Integer> listMap = new HashMap<String, Integer>();
		try {
			String ps = request.getParameter("ps");
			String cp = request.getParameter("cp");
			
			if (cp == null || cp.trim().equals("")) {
				cp = "1";
			}
			if (ps == null || ps.trim().equals("")) {
				ps = "5";
			}
			try {
				int pagesize = Integer.parseInt(ps);
				int cpage = Integer.parseInt(cp);
				QnaDao qnaDao = sqlsession.getMapper(QnaDao.class);
				int totalcount = qnaDao.totallistCount();
				int pagecount = 0;
				
				if (totalcount % pagesize == 0) {
					pagecount = totalcount / pagesize;
				} else {
					pagecount = (totalcount / pagesize) + 1;
				}
				System.out.println("cpage : " + cpage);
				System.out.println("pagesize : " + pagesize);
				int start = cpage * pagesize - (pagesize - 1); // 1*5 -(5-1) = 1
				int end = cpage * pagesize; // 1 * 5 = 5
				
				listMap.put("start", start);
				listMap.put("end", end);
				
				qnaList = qnaDao.qnaList(listMap);
				
				request.setAttribute("qnaList", qnaList);
				request.setAttribute("pagesize", pagesize);
				request.setAttribute("pagecount", pagecount);
				request.setAttribute("cpage", cpage);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println(qnaList.toString());
		model.addAttribute("qnaList", qnaList);
	}
	
	// 상세 페이지 출력
	public void qnaDetail(Model model, String qna_seq) {
		QnaDto qna = null;
		List<QnaReplyDto> qnaReplyList = null;
		try {
			QnaDao qnaDao = sqlsession.getMapper(QnaDao.class);
			qna = qnaDao.qna(qna_seq);
			qnaReplyList = qnaDao.qnaReplyList(Integer.parseInt(qna_seq));			
		}catch(Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("qna", qna);
		model.addAttribute("qnaReplyList", qnaReplyList);
	}
	
	// 글 삭제
	public void qnadelete(String qna_seq) {
		try {
			QnaDao qnaDao = sqlsession.getMapper(QnaDao.class);
			qnaDao.qnaDelete(Integer.parseInt(qna_seq));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 댓글 작성
	public void qnaReply(String user_id, String qna_seq, String qna_reply_content) {
		Map<String, String> replyHash = new HashMap<String, String>();
		try {
			QnaDao qnaDao = sqlsession.getMapper(QnaDao.class);
			replyHash.put("user_id", user_id);
			replyHash.put("qna_seq", qna_seq);
			replyHash.put("qna_reply_content", qna_reply_content);
			qnaDao.qnaReply(replyHash);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
