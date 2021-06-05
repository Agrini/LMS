package com.LMS.lms.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.LMS.lms.exception.BookAlreadyExistsInLibraryException;
import com.LMS.lms.exception.NoRecordsFoundException;
import com.LMS.lms.mapper.BookMapper;
import com.LMS.lms.mapper.RequestMapper;
import com.LMS.lms.model.Books;
import com.LMS.lms.model.Request;

public class RequestServiceDaoImpl implements IRequestServiceDao {
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public List<Request> adminViewRequest() throws NoRecordsFoundException {
		String sql = "select * from requests";
		List<Request> requests = jdbcTemplate.query(sql, new RequestMapper());
		
		if(requests.isEmpty()) {
			throw new NoRecordsFoundException();
		}
		
		return requests;
	}

	@Override
	public boolean removeRequestsByAdmin(String bookName, String bookAuthor) throws Exception{
		String sql = "delete from requests where book_name like '%"+bookName+"%' and book_author like '%"+bookAuthor+"%'";
		
		jdbcTemplate.update(sql);
		
		return true;
	}

	@Override
	public boolean addRequestByMember(String memberMailId, String bookName, String bookAuthor) throws BookAlreadyExistsInLibraryException, Exception {
		String sql = "select * from books where book_name like '%"+bookName+"%' and book_author like '%"+bookAuthor+"%'";
		List<Books> bookList = jdbcTemplate.query(sql, new BookMapper());
		
		if(bookList.isEmpty()) {
			String s = "insert into requests values (?,?,?,'N')";
			int update = jdbcTemplate.update(s, memberMailId, bookName, bookAuthor);
			
			if (update < 0) {
				throw new Exception();
			}
			
			return true;
		}
		
		throw new BookAlreadyExistsInLibraryException();
	}

	@Override
	public List<Request> viewRequest(String memberMailId) throws NoRecordsFoundException {
		String sql = "select * from requests where member_mail_id='"+memberMailId+"'";
		List<Request> requestList = jdbcTemplate.query(sql, new RequestMapper());
		
		if(requestList.isEmpty()) {
			throw new NoRecordsFoundException();
		}
		
		return requestList;
	}

	@Override
	public boolean removeRequestByMember(String memberMailId, String bookName, String bookAuthor) throws Exception {
		String sql = "delete from requests where member_mail_id=? and bookName=? and bookAuthor=?";
		int update = jdbcTemplate.update(sql, memberMailId, bookName, bookAuthor);
		
		if(update < 1) {
			throw new Exception();
		}
		
		return true;
	}

}