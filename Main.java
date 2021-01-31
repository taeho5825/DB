import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	static Connection conn = null;
	static Statement stmt = null;
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		//입력용도
		Scanner sc = new Scanner(System.in);
		ArrayList<String> strList = new ArrayList<>();
		//=============================== 접속정보===========================
		String url = "jdbc:mysql://127.0.0.1:3306/board?serverTimezone=UTC"; //접속할 DBMS주소
		String id = "sbsst";
		String pw = "sbs123414";
		//====================================================================
		
		//String id = "root";
		//String pw = "";
		
		//===============================접속 시도============================
		Class.forName("com.mysql.cj.jdbc.Driver"); //mysqlDriver를 찾는다.
		conn = DriverManager.getConnection(url, id, pw); //특정 DBMS에 접속
		// ==============================sql처리 ==================================
		
		while(true) {
			System.out.println("명령어 : ");
			String cmd = sc.nextLine();
			
			if(cmd.equals("add")) {
				
				System.out.println("제목을 입력해주세요 : ");
				String title = sc.nextLine();
				
				System.out.println("내용을 입력해주세요 : ");
				String body = sc.nextLine();
				
				String sql = "INSERT INTO t_article SET title = '"+ title +"', `body` = '"+ body +"', regDate = NOW()"; 
				
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);
				//System.out.println("저장할 값 : ");
				//String text = sc.nextLine();
				
				//String sql = "insert into test set title = '', `body` = '" + text + "'";
				
				//Statement stmt = conn.createStatement();
				
				//stmt.executeUpdate(sql);
				//DB 저장 - insert
			}
			else if(cmd.equals("list")){
				//DB에 있는 데이터 가져와서 출력 - select
				
				String sql = "SELECT *\r\n" + 
						"FROM t_article";
				
				stmt = conn.createStatement();
				
				System.out.println(conn);
				System.out.println(sql);
				System.out.println(stmt);
				try {
					ResultSet rs = stmt.executeQuery(sql);					
					while(rs.next()) { //rs.next() 가리키는 것이 없을 때, false, 있을때 true
						System.out.println("번호 : " + rs.getInt("id"));
						System.out.println("제목 : " + rs.getString("title"));
						System.out.println("==============================");
					}
				} catch(Exception e) {
					System.out.println("?????????????");
					e.printStackTrace();
				}
				
			}
			else if(cmd.equals("update")) {

				System.out.println("수정할 게시물 번호를 입력해주세요: ");
				
				//게시물 번호로 게시물 찾기
				int aid = Integer.parseInt(sc.nextLine());
				
				String sql = "select * from t_article where id = " + aid;
				
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				
				if(isExistArticleById(aid)) {
					System.out.println("수정할 제목을 입력해주세요 : ");
					String title = sc.nextLine();
					
					System.out.println("수정할 내용을 입력해주세요 : ");
					String body = sc.nextLine();
					
					stmt = conn.createStatement();
					
					String sql2 = "UPDATE t_article\r\n" + 
							"SET title= '"+ title +"',\r\n" + 
							"`body` = '"+ body +"'\r\n" + 
							"WHERE id = 1";
					
					stmt.executeUpdate(sql2);
					
				}else {
					System.out.println("없는 게시물 입니다.");
				}
			}
			else if(cmd.equals("delete")) {
				System.out.println("삭제할 게시물 번호: ");
				int aid = Integer.parseInt(sc.nextLine());
				
				if(isExistArticleById(aid)) {
					String sql = "DELETE\r\n" + 
							"FROM t_article\r\n" + 
							"WHERE id = "+ aid +"";
				}else
				{
					System.out.println("없는 게시물입니다.");
				}
				
				String sql = "delete from article where id = ''";
				
				 stmt = conn.createStatement();
				 stmt.executeUpdate(sql);
			}
			else if(cmd.equals("read")) {
				System.out.println("상세보기 할 게시물 번호: ");
				int aid = Integer.parseInt(sc.nextLine());
				
				if(isExistArticleById(aid)) {
					String sql = "select * from article where id = ''";
					
					stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql);
					
					rs.next();
					int targetId = rs.getInt("id");
					String title = rs.getString("title");
					String body = rs.getString("body");
					String regDate = rs.getString("regDate");
					
					System.out.println("번호 : " + targetId);
					System.out.println("제목 : " + title);
					System.out.println("내용 : " + body);
					System.out.println("작성일 : " + regDate);
				}
				else {
					System.out.println("없는 게시물입니다.");
				}
			}
		}
	}
	private static boolean isExistArticleById(int aid) throws SQLException {
		
		String sql = "select * from t_article where id = " + aid;
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		if(rs.next()) {
			return true;
		}
		return false;
	}
}
