package board;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.opensymphony.xwork2.ActionSupport;

public class deleteAction extends ActionSupport{
	public static Reader reader;
	public static SqlMapClient sqlMapper;
	
	private boardVO paramClass; //파라미터를 저장할 객체
	private boardVO resultClass; //쿼리 결과 값을 저장할 객체
	
	private int currentPage; //현재 페이지
	
	private String fileUploadPath="C:/java/upload/";
	
	private int no;
	
	public String execute() throws Exception {
		paramClass = new boardVO();
		resultClass = new boardVO();
		
		resultClass = (boardVO) sqlMapper.queryForObject("selectOne",getNo());
		
		//서버 파일 삭제(로컬?)
		File deleteFile = new File(fileUploadPath + resultClass.getFile_savename());
		deleteFile.delete();
		
		//삭제할 항목 설정
		paramClass.setNo(getNo());
		//삭제 쿼리 수행
		sqlMapper.update("deleteBoard",paramClass);
		
		return SUCCESS;
	}
	public boardVO getParamClass() {
		return paramClass;
	}

	public void setParamClass(boardVO paramClass) {
		this.paramClass = paramClass;
	}

	public boardVO getResultClass() {
		return resultClass;
	}

	public void setResultClass(boardVO resultClass) {
		this.resultClass = resultClass;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getFileUploadPath() {
		return fileUploadPath;
	}

	public void setFileUploadPath(String fileUploadPath) {
		this.fileUploadPath = fileUploadPath;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public deleteAction() throws IOException{
		reader = Resources.getResourceAsReader("sqlMapConfig.xml");
		sqlMapper = SqlMapClientBuilder.buildSqlMapClient(reader);
		reader.close();
	}
}
