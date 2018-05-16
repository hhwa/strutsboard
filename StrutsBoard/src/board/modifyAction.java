package board;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.io.FileUtils;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.opensymphony.xwork2.ActionSupport;

public class modifyAction extends ActionSupport{
	
	public static Reader reader;
	public static SqlMapClient sqlMapper;
	
	private boardVO paramClass; //파라미터를 저장할 객체
	private boardVO resultClass; //쿼리 결과값을 저장할 객체
	
	private int currentPage; //현재 페이지
	
	private int no;
	private String subject;
	private String name;
	private String password;
	private String content;
	private String old_file;
	
	private File upload; //파일 객체
	private String uploadContentType; //컨텐츠 타입
	private String uploadFileName; //파일이름
	private String fileUploadPath ="C:/Java/upload/"; //업로드 경로
			
	public modifyAction() throws IOException{
		reader = Resources.getResourceAsReader("sqlMapConfig.xml"); // sqlMapConfig.xml 파일의 설정내용을 가져온다.
		sqlMapper = SqlMapClientBuilder.buildSqlMapClient(reader); // sqlMapConfig.xml의 내용을 적용한 sqlMapper 객체 생성.
		reader.close();
	}
	
	//게시글 수정
	public String execute() throws Exception {
		paramClass = new boardVO();
		resultClass = new boardVO();
		
		//수정할 항목 설정
		paramClass.setNo(getNo());
		paramClass.setSubject(getName());
		paramClass.setName(getName());
		paramClass.setPassword(getPassword());
		paramClass.setContent(getContent());
		
		//일단 항목만 수정한다.
		sqlMapper.update("updateBoard",paramClass);
		
		//수정할 파일이 업로드 되었다면 파일을 업로드 하고 DB의 file항목을 수정함
		if(getUpload() != null) {
			//실제 서버에 저장될 파일 이름 확장자 설정.
			String file_name = "file_"+getNo();
			String file_ext = getUploadFileName().substring(getUploadFileName().lastIndexOf('.')+1, getUploadFileName().length());
			
			//이전 파일 삭제
			File deleteFile = new File(fileUploadPath = getOld_file());
			deleteFile.delete();
			
			//새파일 업로드
			File destFile = new File(fileUploadPath+file_name+"."+file_ext);
			FileUtils.copyFile(getUpload(), destFile);
			
			//파일정보파라미터 설정.
			paramClass.setFile_orgname(getUploadFileName());
			paramClass.setFile_savename(file_name+"."+file_ext);
			
			//파일 정보 업데이트
			sqlMapper.update("updateFile",paramClass);
		}
		
		//수정이 끝나면 view 페이지로 이동
		resultClass = (boardVO) sqlMapper.queryForObject("selectOne",getNo());
		
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

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOld_file() {
		return old_file;
	}

	public void setOld_file(String old_file) {
		this.old_file = old_file;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getFileUploadPath() {
		return fileUploadPath;
	}

	public void setFileUploadPath(String fileUploadPath) {
		this.fileUploadPath = fileUploadPath;
	}

}
