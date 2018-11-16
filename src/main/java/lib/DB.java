package lib;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Long
 * Date: 11/15/2018
 * Time: 11:23 AM
 */
public class DB {
    private String DATABASE = "long_book"; // Database name
    private String HOST = "localhost"; //host
    private String PORT = "3306"; //cổng
    private String USER = "root"; // username
    private String PASSWORD = "mysql"; //Mật khẩu
    private String DB_URL;

    private Statement state = null; //Statement ~ một câu lệnh SQL
    private Connection conn = null; //Biến kết nối
    private ResultSet result = null; //ResultSet đại diện cho tập hợp các bản ghi lấy do thực hiện truy vấn.
    private ArrayList<ArrayList<String>> kq = null; //Danh sách trả về các bản ghi


    /* public static void main(String[] args) {
         DB db = new DB();
         System.out.println(db.isConnected());
         if (db.select("id,name, username, password", "admin", " 1")) {
             for (ArrayList<String> record : db.getResult()) {
                 System.out.println(record);
             }
         }

         try{
             Thread.sleep(20000);
         } catch (Exception ex){}
         System.out.println(db.isConnected());
         db.autoReconect();
         System.out.println(db.isConnected());

 //        db.freeConnection();
     }
 */
    public DB() {
        System.setProperty("file.encoding", "UTF-8");
        DB_URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?zeroDateTimeBehavior=convertToNull&autoReconnect=true&useSSL=false&characterEncoding=utf8"; //Địa chỉ DataBase
//            System.out.println(DB_URL);
        this.getConnection(3);
    }


    /**
     * Hàm kết nối vào csdl Tạo 1 kết nối, nếu thất bại kết nối lại n lần Là 1
     * hàm mở rộng của hàm dưới
     *
     * @param n the value of n
     * @return the boolean: có kết nối được không
     */
    private boolean getConnection(int n) {
        for (int i = 0; i < n; i++) {
            if (this.getConnection()) {
                System.out.println("Connected");
                return true;
            } else {
                try {
                    System.out.println("Connecting to database");
                    Thread.sleep(5000 * i);
                } catch (InterruptedException ex) {
//                        System.out.println(ex);
                }
            }
        }
        return false;
    }

    /**
     * Tạo 1 kết nối
     *
     * @return the boolean: có kết nối được không
     */
    public boolean getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); ////Đăng kí drive
            this.conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            this.state = this.conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.TYPE_SCROLL_SENSITIVE); //Khởi tạo state
//                System.out.println("Database Connected");
            return true;
        } catch (Exception e) {
//                System.err.println("Could not create connection to " + DATABASE + "\n" + e);
        }
        return false;
    }

    /**
     * Giải phóng kết nối khi không dùng tới Nếu có kết nối tới server thông qua
     * ssh thì cũng giải phóng kết nối ssh này luôn
     */
    public void freeConnection() {
        try {
            this.state.close();
            this.conn.close();
        } catch (Exception e) {
//            System.err.println("Không thể đóng kết nối của " + DATABASE + "\n" + e);
        }
    }

    /**
     * Thực thi 1 câu truy vẫn có trả về dữ liệu
     *
     * @param query : câu truy vấn sql
     * @return the boolean: thành công/thất bại
     */
    public boolean executeQuery(String query) {
        //Nếu câu truy vấn không trả về dữ liệu thì nên sử dụng state.execute(sql);, bằng không sẽ báo lỗi
        try {
            this.result = this.state.executeQuery(query);
            return true;
        } catch (SQLException ex) {
            System.err.println("Đã có lỗi xảy ra khi thực thi câu lệnh:\n" + query + "\n ở hàm excuteQuery(String query)");
        }
        return false;

    }

    /**
     * Thực thi 1 câu truy vẫn không trả về dữ liệu(update,delete,insert...)
     *
     * @param query : câu truy vấn sql
     * @return the boolean: thành công/thất bại
     */
    public boolean execute(String query) {
        try {
            this.state.execute(query);
            return true;
        } catch (SQLException ex) {
            System.err.println("Đã có lỗi xảy ra khi thực thi câu lệnh:\n" + query + "\n ở hàm excute(String query)");
        }
        return false;

    }


    /**
     * Truy vấn select (phần chính)
     *
     * @param column    cột cần select , có thể select *, distinct(id) , ...
     * @param table     bảng đang được select
     * @param condition điều kiện select
     * @return the boolean : thành công/thất bại
     */
    public boolean select(String column, String table, String condition) {
        try {
            String sql = "SELECT " + column + " FROM " + table + " WHERE " + condition + " ;"; //Câu lệnh sql

            if (!this.executeQuery(sql)) {//Thực thi câu lệnh truy vấn
                return false;
            }
            if (this.result == null) {
                return false;
            }
            //Số cột của mỗi bản ghi
            int totalColumn = this.result.getMetaData().getColumnCount();
            if (totalColumn == 0) {
                return false;
            }
            kq = new ArrayList<ArrayList<String>>();
            this.result.beforeFirst();
            while (this.result.next()) {//Vòng lặp
                ArrayList<String> temKq = new ArrayList<String>();//Biến lưu kết quả tạm thời
                ResultSetMetaData metaData = this.result.getMetaData();//Đại diện cho 1 bản ghi
                for (int j = 1; j <= totalColumn; j++) {
                    String thisKQ = this.result.getString(j);

                    //Đối với cột YEAR(4) thì nó sẽ hiển thị dạng 2018-01-01 nên cần loại bỏ các số -01-01 đằng sau
                    if ("java.sql.Date".equalsIgnoreCase(metaData.getColumnClassName(j)) && "YEAR".equalsIgnoreCase(metaData.getColumnTypeName(j)) && metaData.getPrecision(j) == 4) {
                        if (thisKQ != null && thisKQ.contains("-")) {
                            thisKQ = thisKQ.substring(0, thisKQ.indexOf("-"));
                        }
                    }
                    temKq.add(thisKQ);
                }
                if (temKq.size() > 0) {
                    kq.add(temKq);
                }
            }
            return kq != null && kq.size() > 0;
        } catch (Exception ex) {
//            System.out.println(ex);
            return false;
        }
    }

    /**
     * Hàm insert con
     */
    public boolean insert(String table, ArrayList<String> colName, ArrayList<String> colValue) throws SQLException { //Truy vấn insert
        return insert(table, colName.toArray(new String[colName.size()]), colValue.toArray(new String[colValue.size()]));
    }

    /**
     * Hàm insert con
     */
    public boolean insert(String table, ArrayList<String> colName, String[] colValue) throws SQLException { //Truy vấn insert
        return insert(table, colName.toArray(new String[colName.size()]), colValue);
    }

    /**
     * Hàm insert con
     */
    public boolean insert(String table, String[] colName, ArrayList<String> colValue) throws SQLException { //Truy vấn insert
        return insert(table, colName, colValue.toArray(new String[colValue.size()]));
    }

    /**
     * Thay thế 1 số ký tự đặc biệt có trong giá trị value
     *
     * @param value : dữ liệu thô, lấy từ web hoặc do người dùng nhập vào
     */
    public static String validSql(String value) {
        if (value == null) {
            return null;
        }
        //Các câu lệnh sau phải đúng thứ tự
        value = value.replace("\\", "\\\\");
        value = value.replace("\"", "\\\"");
        value = value.replace("\'", "\\\'");
        return value;
    }

    /**
     * Hàm chính Insert bản ghi vào database
     *
     * @param table    : bảng muốn insert
     * @param colName  : danh sách các tên cột của các giá trị insert
     * @param colValue : danh sách các giá trị insert
     * @return true/false: thành công/thất bại
     */
    public boolean insert(String table, String[] colName, String[] colValue) { //Truy vấn insert
        try {
            int totalColumn = colValue.length;
            if (colName.length != totalColumn) {
                System.err.println("Số lượng các cột không trùng nhau");
                return false;
            }

            String column = "";//Tên các cột
            String strColValue = "";//Giá trị các cột
            for (int i = 0; i < totalColumn; i++) {
                if (colName[i] != null && colValue[i] != null) {
                    strColValue += "\"" + validSql(colValue[i]) + "\"";
                    column += colName[i].trim();
                    if (i < totalColumn - 1) {
                        column += ", ";
                        strColValue += ", ";
                    }
                }
            }
            column = column.trim();
            strColValue = strColValue.trim();
            while (column.substring(column.length() - 1).equals(",")) {
                column = column.substring(0, column.length() - 1).trim();
            }
            while (strColValue.substring(strColValue.length() - 1).equals(",")) {
                strColValue = strColValue.substring(0, strColValue.length() - 1).trim();
            }
            while (column.substring(0, 1).equals(",")) {
                column = column.substring(1).trim();
            }
            while (strColValue.substring(0, 1).equals(",")) {
                strColValue = strColValue.substring(1).trim();
            }
            String sql = "INSERT INTO " + table + "(" + column + ")" + " VALUES " + "(" + strColValue + ");";

            return this.execute(sql);//Thực thi câu lệnh insert
        } catch (Exception ex) {
//            System.out.println(ex);
            return false;

        }
    }

    public boolean update(String table, ArrayList<String> col, ArrayList<String> colValue, String condition) { //Truy vấn update, các giá trị null thì không update
        return update(table, col.toArray(new String[col.size()]), colValue.toArray(new String[colValue.size()]), condition);
    }

    public boolean update(String table, String[] colName, ArrayList<String> colValue, String condition) { //Truy vấn update, các giá trị null thì không update
        return update(table, colName, colValue.toArray(new String[colValue.size()]), condition);
    }

    public boolean update(String table, ArrayList<String> colName, String[] colValue, String condition) { //Truy vấn update, các giá trị null thì không update
        return update(table, colName.toArray(new String[colName.size()]), colValue, condition);
    }

    /**
     * Hàm chính Truy vấn update, các giá trị null thì không update
     *
     * @param table     : tên bảng
     * @param colName   : danh sách tên các cột
     * @param colValue  : danh sách các giá trị
     * @param condition : điều kiện update
     * @return the boolean
     */
    public boolean update(String table, String[] colName, String[] colValue, String condition) {
        try {
            int totalColumn = colValue.length;
            if (colName.length != totalColumn) {
                System.err.println("Số lượng các cột không trùng nhau");
                return false;
            }

            String set = "";
            for (int i = 0; i < totalColumn; i++) {
                String tempColName = colName[i];
                String tempColValue = colValue[i];

                if (tempColValue != null && tempColName != null) {
                    set += tempColName + " = \"" + validSql(tempColValue) + "\"";
                    if (i < totalColumn - 1) {
                        set += ", ";
                    }
                }
            }
            //Loại bỏ các dấu , thừa trong set
            set = set.trim();
            while (set.substring(set.length() - 1).equals(",")) {
                set = set.substring(0, set.length() - 1).trim();
            }
            while (set.substring(0, 1).equals(",")) {
                set = set.substring(1).trim();
            }

            if (condition == null || condition.trim().equals("")) {
                condition = "1";
            }
            String sql = "UPDATE " + table + " SET " + set + " WHERE " + condition;

            return this.execute(sql);//Thực thi câu lệnh update
        } catch (Exception ex) {
//            System.out.println(ex);
            return false;
        }
    }

    /*
    * insert or update records
    * Nếu bản ghi đã tồn tại trong csdl thì {
    * - nếu bản ghi có sự khác biệt với dữ liệu insert vào thì update lại
    * - nếu bản ghi không có sự khác biệt thì không làm gì cả
    }
    * Còn bản ghi chưa có thì insert
    * inodate = insert + or + update
     */
    public boolean inodate(String table, String[] colName, String[] colValue, String condition) {
        int totalColumn = colValue.length;
        if (colName.length != totalColumn) {
            System.err.println("Số lượng các cột không trùng nhau");
            return false;
        }
        String column = "";
        for (int i = 0; i < totalColumn; i++) {
            column += colName[i];
            if (i < totalColumn - 1) {
                column += ", ";
            }
        }
        if (this.select(column, table, condition)) {
            //Bản ghi đã có trong csdl
            ArrayList<String> a = this.kq.get(0);
//            System.out.println(a);
            boolean update = false;//Biến quyết định thực thi lệnh update
            ArrayList<String> colNamesArrayList = new ArrayList<String>();
            ArrayList<String> colValuesArrayList = new ArrayList<String>();
            for (int i = 0; i < a.size(); i++) {
                String colDBName = colName[i];//Tên của cột chứa giá trị này trong DB
//                System.out.println(a.get(i));
                String valueDB = a.get(i);//Giá trị trong db
                String valueOut = colValue[i];//Giá trị bên ngoài đang muốn thêm vào
                if (valueDB == null) {
                    if (valueOut != null) {
                        update = true;
                        colNamesArrayList.add(colDBName);//Tên của cột có giá trị này
                        colValuesArrayList.add(valueOut);//Giá trị của giá trị này
                    }
                } else if (!valueDB.equals(valueOut)) {//Nếu hai giá trị trong - ngoài khác nhau (so sánh theo kiểu string)
                    {
                        update = true;
                        if (colValue[i] != null) {
                            colNamesArrayList.add(colDBName);
                            colValuesArrayList.add(colValue[i].trim());
                        }
                    }
                }
            }
            //Nếu bản ghi có sự khác biệt với dữ liệu update thì mới update
            //Còn bản ghi giống hệt thì không làm gì cả
            if (update) {
                return (colNamesArrayList.size() == 0) ? true : this.update(table, colNamesArrayList, colValuesArrayList, condition);
            }
        } else {
            return this.insert(table, colName, colValue);
        }
        return true;

    }

    public ArrayList<ArrayList<String>> getResult() {
        return this.kq;
    }

}
