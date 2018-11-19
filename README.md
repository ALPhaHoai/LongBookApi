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



# API Restful Service

## 1. Book Node

### 1. Get all Book

  Lấy thông tin danh sách các truyện

* **URL**

  /book

* **Authentication**

  `No`

* **Method:**
  
  `GET`
  
*  **URL Params**

   **Optional:**
 
   `start=[0-~] unsigned integer default:0`
   
   `limit=[1-100] unsigned integer default:10`

* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:**
    
        {
            "result": 
            [
                {
                    "id": 4,
                    "title": "Khí Phi Hồ Sủng",
                    "content": "Nội dung truyện Khí Phi Hồ Sủng"
                },
                {
                    "id": 5,
                    "title": "Mê Muội",
                    "content": "Nội dung truyện Mê Muội"
                }
            ],
            "message": "OK",
            "status": 200
        }
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:** 
    
        {
            "message": "Empty data",
            "status": 400
        }

* **Sample Call:**

   `GET` /book <br />
   `GET` /book?start=1&limit=2



### 2. Get a specified book

  Lấy thông tin của 1 truyện cụ thể

* **URL**

  /book/{book_id}

* **Authentication**

  `No`

* **Method:**
  
  `GET`
  
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
        {
            "result": 
            {
                "id": 10,
                "title": "Tên truyện",
                "content": "Nội dung truyện"
            },
            "message": "OK",
            "status": 200
        }
 
* **Error Response:**

  * **Code:** 404 Not Found <br />
    **Content:**
    
        {
            "message": "Book not found",
            "status": 404
        }

* **Sample Call:**

   `GET` /book/1 <br />


### 3. Insert book

  Thêm 1 truyện

* **URL**

  /book

* **Authentication**

  `Yes`

* **Method:**
  
  `POST`
  
*  **Data Params**

   **Required:**
   
       {
           "title": {Tên truyện},
           "content": {Nội dung truyện}
       }

* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
        {
            "result": 
            {
                "id": 10,
                "title": "Tên truyện",
                "content": "Nội dung truyện"
            },
            "message": "OK",
            "status": 200
        }
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
  
    **Content:** 
    
        {
            "status": 400,
            "message": "Duplicate content with book 2"
        }
    
      OR
    
        {
            "status": 400,
            "message": "Invalid input data"
        }
        
      OR
    
        {
            "status": 400,
            "message": "Can't insert this book"
        }
        

* **Sample Call:**

   `POST` /book
       
       {
            "title": "Con cò",
            "content": "Con cò bay lả bay la"
       }



### 4. Update book

  Update 1 truyện

* **URL**

  /book/{book_id}

* **Authentication**

  `Yes`

* **Method:**
  
  `PUT`
  
*  **Data Params**

   **Required:**
   
       {
           "title": {Tên truyện},
           "content": {Nội dung truyện}
       }

* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
        {
            "result": 
            {
                "id": 22,
                "title": "Đỉnh Luyện Thần Ma 2",
                "content": "Nội dung truyện đã chỉnh sửa"
            },
            "message": "OK",
            "status": 200
        }
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:** 
    
        {
            "status": 400,
            "message": "Duplicate content with book 2"
        }
    
      OR
    
        {
            "status": 400,
            "message": "Invalid input data"
        }
       
      OR
    
        {
            "status": 400,
            "message": "Can't update this book"
        }
        

* **Sample Call:**

   `PUT` /book/1
   
        {
            "title": "Con cò",
            "content": "Con cò bay lả bay la"
        }



### 5. Delete a specified book

  Xóa một truyện

* **URL**

  /book/{book_id}

* **Authentication**

  `Yes`

* **Method:**
  
  `DELETE`
  
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
        {
            "status": 200,
            "message": "Delete this book successful"
        }
        
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:** 
    
        {
            "status": 400,
            "message": "This book doesn't exist" 
        }
        
      OR
    
        {
            "status": 400,
            "message": "Can't delete this book"
        }
        

* **Sample Call:**

   `DELETE` /book/1 <br />


### 6. Show of category of specified book

  Xem thông tin các thể loại của một truyện cụ thể

* **URL**

  /book/{book_id}/category

* **Method:**
  
  `GET`

* **Authentication**

  `No`

*  **URL Params**

   **Optional:**
 
   `start=[0-~] unsigned integer default:0`
   
   `limit=[1-100] unsigned integer default:10`
   
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
        {
            "result": 
            [
                {
                    "name": "Ngôn Tình",
                    "id": 1
                },
                {
                    "name": "Truyện Teen",
                    "id": 9
                }
            ],
            "message": "OK",
            "status": 200
        }
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:** 
    
        {
            "status": 400, 
            "message": "This book doesn't exist"
        }
    
      OR
    
        {
            "status": 400,
            "message": "Empty data"
        }
        

* **Sample Call:**

   `GET` /book/1/category <br />
   `GET` /book/1/category?start=0&limit=2 <br />
   

### 7. Insert a category of specified book

  Thêm thể một thể loại cho một truyện cụ thể

* **URL**

  /book/{book_id}/category

* **Authentication**

  `Yes`

* **Method:**
  
  `POST`
  

*  **Data Params**

   **Required:**
   
       {
           "category_id": {category_id}
       }
   
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
        {
            "status": 200,
            "message": "Insert this category for this book successful"
        }
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:** 
    
        {
            "status": 400,
            "message": "This book is already has this category"
        }
    
      OR
    
        {
            "status": 400,
            "message": "Can't insert this category for this book"
        }
        

* **Sample Call:**

   `POST` /book/1/category
   
        {
            "category_id": 4
        }

### 8. Delete all category of specified book

  Xóa tất cả các thể loại của một truyện cụ thể

* **URL**

  /book/{book_id}/category

* **Authentication**

  `Yes`

* **Method:**
  
  `DELETE`
  
   
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
        {
            "status": 200,
            "message": "Delete all category of this book successful"
        }
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:** 
    
        {
            "status": 400,
            "message": "This book has no category"
        }
    
      OR
    
        {
            "status": 400,
            "message": "Can't delete all category of this book"
        }
        

* **Sample Call:**

   `DELETE` /book/1/category <br />
   
### 9. Delete a category of specified book

  Xóa một thể loại của một truyện cụ thể

* **URL**

  /book/{book_id}/category/{category_id}

* **Authentication**

  `Yes`

* **Method:**
  
  `DELETE`
   
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
        {
            "status": 200,
            "message": "Delete this category of this book successful"
        }
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:** 
    
        {
            "status": 400,
            "message": "This book is not has this category"
        }
    
      OR
    
        {
            "status": 400,
            "message": "Can't delete this category for this book"
        }
        

* **Sample Call:**

   `DELETE` /book/1/category/2 <br />
   
      
## 2. Category Node

### 1. Get all category

  Lấy thông tin tất cả các thể loại

* **URL**

  /category

* **Authentication**

  `No`

* **Method:**
  
  `GET`
  
*  **URL Params**

   **Optional:**
 
   `start=[0-~] unsigned integer default:0`
   
   `limit=[1-100] unsigned integer default:10`

* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
        {
            "result": 
            [
                {
                    "name": "Ngôn Tình",
                    "id": 1
                },
                {
                    "name": "Xuyên Không",
                    "id": 2
                }
            ],
            "message": "OK",
            "status": 200
        }
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:**
    
        {
            "status": 400,
            "message": "Empty data"
        }

* **Sample Call:**

   `GET` /category <br />
   `GET` /category?start=1&limit=2


### 2. Get a specified category

  Lấy thông tin cụ thể của 1 thể loại

* **URL**

  /book/{category_id}

* **Authentication**

  `No`

* **Method:**
  
  `GET`
  
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:**
    
        {
            "result": 
            {
                "name": "Ngôn Tình",
                "id": 1
            },
            "message": "OK",
            "status": 200
        }
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:**
    
        {
            "status": 400,
            "message": "This category doesn't exist"
        }

* **Sample Call:**

   `GET` /category/1 <br />


### 3. Insert category

  Thêm 1 thể loại

* **URL**

  /category

* **Authentication**

  `Yes`

* **Method:**
  
  `POST`
  
*  **Data Params**

   **Required:**
   
       {
           "name": {Tên thể loại}
       }

* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:**
    
        {
            "result": 
            {
                "name": "Tên thể loại",
                "id": 17
            },
            "message": "OK",
            "status": 200
        }
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
  
    **Content:** 
    
        {
            "status": 400,
            "message": "Duplicate content with category 2"
        }
    
      OR
    
        {
            "status": 400,
            "message": "Invalid input data"
        }
        
      OR
    
        {
            "status": 400,
            "message": "Can't insert this category"
        }
        

* **Sample Call:**

   `POST` /category
   
        {
            "name": "Truyện cười"
        }



### 4. Update category

  Update 1 thể loại

* **URL**

  /book/{category_id}

* **Authentication**

  `Yes`

* **Method:**
  
  `PUT`
  
*  **Data Params**

   **Required:**
   
       {
           "name": {Tên thể loại mới}
       }

* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:**
    
        {
            "result": 
            {
                "name": "Tên thể loại mới",
                "id": 2
            },
            "message": "OK",
            "status": 200
        }
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:** 
    
        {
            "status": 400,
            "message": "Duplicate content with category 2"
        }
    
      OR
    
        {
            "status": 400,
            "message": "Invalid input data"
        }
        

* **Sample Call:**

   `PUT` /category/1
   
         {
            "name": "Truyện cười"
         }



### 5. Delete a specified category

  Xóa một thể loại

* **URL**

  /category/{category_id}

* **Authentication**

  `Yes`

* **Method:**
  
  `DELETE`
  
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:**
    
        {
            "status": 200,
            "message": "Delete this category successful"
        }
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:** 
    
        {
            "status": 400,
            "message": "This category doesn't exist" 
        }
        
      OR
    
        {
            "status": 400,
            "message": "Can't delete this category"
        }
        

* **Sample Call:**

   `DELETE` /category/1 <br />

### 6. Show all book of specified category

  Xem danh sách các truyện thuộc một thể loại cụ thể

* **URL**

  /category/{category_id}/book

* **Authentication**

  `No`

* **Method:**
  
  `GET`
  
*  **URL Params**

   **Optional:**
 
   `start=[0-~] unsigned integer default:0`
   
   `limit=[1-100] unsigned integer default:10`
   
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:**
    
        {
            "result": 
            [
                {
                    "id": 7,
                    "title": "Ông Xã Là Trung Khuyển",
                    "content": "Nội dung truyện Ông Xã Là Trung Khuyển"},
                {
                    "id": 17,
                    "title": "Gian Nịnh Quốc Sư Yêu Tà Thê",
                    "content": "Nội dung truyện Gian Nịnh Quốc Sư Yêu Tà Thê"
                }
            ],
            "message": "OK",
            "status": 200
        }
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:** 
    
        {
            "status": 400, 
            "message": "This category doesn't exist"
        }
    
      OR
    
        {
            "status": 400,
            "message": "Empty data"
        }
        

* **Sample Call:**

   `GET` /category/1/book <br />
   `GET` /category/1/book?start=0&limit=2 <br />
