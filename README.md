# LongBook
Ứng dụng đọc truyện

Chức năng
  - Người quản trị
    + Có thể thêm mới, chỉnh sửa, xóa truyện
  - Người dùng
    + Xem danh sách truyện
	+ Xem chi tiết nội dung từng truyện
	+ Có thể tra cứu theo thể loại truyện
	
Mô tả hệ thống
1. Backend
  + Xây dựng 1 Web Service để đổ dữ liệu về cho front-end 
  + Cơ dữ liệu Mysql
  + Xây dựng API phục vụ cho người quản trị, và người dùng

2. Front-end
  + Phần mềm quản trị
    - Giao diện để quản lý thêm, sửa, xóa truyện
	- Dùng java swing or trên web
  + Người dùng cuối
     - Giao diện để xem danh sách truyện
	 - Có thể tìm kiếm thể loại truyện
	 - Xem chi tiết truyện
	 - Có thể phát triển trên android hoặc java swing
 
 Yêu cầu
  + Dùng Jersey cho web service
  + Chỉ backend mới có thể tương tác với csdl
  + Backend có thể chỉ cần viết trên console
