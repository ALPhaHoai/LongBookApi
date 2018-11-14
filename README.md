# LongBook
Ứng dụng đọc truyện

Chức năng
  - Người quản trị
    + Có thể thêm mới, chỉnh sửa, xóa truyện
  - Người dùng
    + Xem danh sách truyện
	+ Xem chi tiết nội dung từng truyện
	+ Duyệt danh sách truyện theo thể loại
	
Mô tả hệ thống
1. Backend
  + Xây dựng 1 Web Service để đổ dữ liệu về cho front-end
  + Cơ dữ liệu Mysql
  + Xây dựng API phục vụ cho người quản trị, và người dùng

2. Front-end
  + Phần mềm quản trị
    - Trang web quản trị để quản lý thêm, sửa, xóa truyện

  + Người dùng cuối
     - Android App xem danh sách truyện
	 - Có thể tìm kiếm teo tên
	 - Duyệt danh sách truyện theo thể loại
	 - Xem chi tiết truyện
 
 Yêu cầu
  + Dùng Jersey cho web service
  + Chỉ backend mới có thể tương tác với csdl
  + Backend có thể chỉ cần viết trên console
