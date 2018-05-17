package board;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.opensymphony.xwork2.ActionSupport;

public class writeAction extends ActionSupport{

	public static Reader reader; //파일 스트림을 위한 reader.
	public static SqlMapClient sqlMapper; //sqlMapClient API를 사용하기 위한 sqlMapper 객체
	
	private boardVO paramClass; //파라미터를 저장할 객체
	private boardVO resultClass;//쿼리 결과 값을 저장할 객체
	
	private int currrentPage; //현재 페이지
	
	private int no;
	private String subject;
	private String name;
	private String password;
	private String content;
	private String file_orgName; //업로드 파일의 원래 이름
	private String file_saveName; // 서버에 저장할 업로드 파일의 이름. 고유번호로 구분
	Calendar today=Calendar.getInstance(); //오늘 날짜
	private int ref;
	private int re_step;
	private int re_level;
	
	boolean reply = false;

	private File upload; //파일 객체
	private String uploadContentType;  //컨텐츠 타입
	private String uploadFileName;	//파일 이름
	private String fileUploadPath ="C:/Java/upload/";	//업로드 경로
	
	public writeAction() throws IOException {
		reader=Resources.getResourceAsReader("sqlMapConfig.xml");
		sqlMapper=SqlMapClientBuilder.buildSqlMapClient(reader);
		reader.close();
	}
	public String form() throws Exception {
		return SUCCESS;
	}
	public String reply() throws Exception {
		
		reply=true;
		resultClass = new boardVO();
		
		resultClass = (boardVO) sqlMapper.queryForObject("selectOne",getNo());
		resultClass.setSubject("[답변]"+resultClass.getSubject());
		resultClass.setPassword("");
		resultClass.setName("");
		resultClass.setContent("");
		resultClass.setFile_orgname(null);
		resultClass.setFile_savename(null);
		
		return SUCCESS;
	}
	public String execute() throws Exception {
		paramClass = new boardVO();
		resultClass = new boardVO();
		
		
		if(ref == 0)
		{
			paramClass.setRe_step(0);
			paramClass.setRe_level(0);
			
			
			
		}
		else
		{
			
			paramClass.setRef(getRef());
			paramClass.setRe_step(getRe_step());
			sqlMapper.update("updateReplyStep", paramClass);
			
			paramClass.setRe_step(getRe_step() + 1);
			paramClass.setRe_level(getRe_level() + 1);
			paramClass.setRef(getRef());
		}
		
		paramClass.setSubject(getSubject());
		paramClass.setName(getName());
		paramClass.setPassword(getPassword());
		paramClass.setContent(getContent());
		paramClass.setRegdate(today.getTime());
		
		
		if(ref == 0)
			sqlMapper.insert("insertBoard", paramClass);
		else
			sqlMapper.insert("insertBoardReply", paramClass);
		
		if(getUpload() != null)
		{
			resultClass = (boardVO) sqlMapper.queryForObject("selectLastNo");
			
			String file_name = "file_" + resultClass.getNo();
			String file_ext = getUploadFileName().substring(
					getUploadFileName().lastIndexOf('.') + 1,
					getUploadFileName().length()
					);
			
			File destFile = new File(fileUploadPath + file_name + "." + file_ext);
			FileUtils.copyFile(getUpload(), destFile);
			
			paramClass.setNo(resultClass.getNo());
			paramClass.setFile_orgname(getUploadFileName());
			paramClass.setFile_savename(file_name + "." +file_ext);
			
			sqlMapper.update("updateFile", paramClass);
		}

		return SUCCESS;
	}
	public boolean isReply() {
		return reply;
	}
	public void setReply(boolean reply) {
		this.reply = reply;
	}
	public int getRef() {
		return ref;
	}
	public void setRef(int ref) {
		this.ref = ref;
	}
	public int getRe_step() {
		return re_step;
	}
	public void setRe_step(int re_step) {
		this.re_step = re_step;
	}
	public int getRe_level() {
		return re_level;
	}
	public void setRe_level(int re_level) {
		this.re_level = re_level;
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
	public int getCurrrentPage() {
		return currrentPage;
	}
	public void setCurrrentPage(int currrentPage) {
		this.currrentPage = currrentPage;
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
	public String getFile_orgName() {
		return file_orgName;
	}
	public void setFile_orgName(String file_orgName) {
		this.file_orgName = file_orgName;
	}
	public String getFile_saveName() {
		return file_saveName;
	}
	public void setFile_saveName(String file_saveName) {
		this.file_saveName = file_saveName;
	}
	public Calendar getToday() {
		return today;
	}
	public void setToday(Calendar today) {
		this.today = today;
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
