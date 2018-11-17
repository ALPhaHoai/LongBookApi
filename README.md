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



#API Restful Service

##1. Book Node

###1. Get all Book

  Lấy thông tin danh sách các truyện

* **URL**

  /book

* **Method:**
  
  `GET`
  
*  **URL Params**

   **Optional:**
 
   `start=[0-~] unsigned integer default:0`
   
   `limit=[1-100] unsigned integer default:10`

* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
    ***
        [
            {
                "id" : 1,
                "title" : "Tên 1",
                "content" : "Nội dung truyện 1"
            },
            {
                 "id" : 2,
                 "title" : "Tên 2",
                 "content" : "Nội dung truyện 2"
            }
        ]
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:** 
    
    ***
    
        {
            "error" : true,
            "message" : "Empty data"
        }

* **Sample Call:**

   `GET` /book <br />
   `GET` /book?start=1&limit=2



###2. Get a specific book

  Lấy thông tin của 1 truyện cụ thể

* **URL**

  /book/{book_id}

* **Method:**
  
  `GET`
  
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
    ***
        {
            "id" : 4,
            "title" : "Tên",
            "content" : "Nội dung"
        }
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:**
    
    ***
        {
            "error" : true,
            "message" : "This book doesn't exist"
        }

* **Sample Call:**

   `GET` /book/1 <br />


###3. Insert book

  Thêm 1 truyện

* **URL**

  /book

* **Method:**
  
  `POST`
  
*  **Data Params**

   **Required:**
   
   ***
       {
           "title" : "Tên truyện",
           "content" : "Nội dung truyện"
       }

* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
    ***
        { 
            "id" : 2,
            "title" : "Tên truyện",
            "content" : "Nội dung truyện" 
        }
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
  
    **Content:** 
    
        {
            "error" : true,
            "message" : "Duplicate content with book 2"
        }
    
      OR
    
        {
            "error" : true,
            "message" : "Invalid input data"
        }
        
      OR
    
        {
            "error" : true,
            "message" : "Can't insert this book"
        }
        

* **Sample Call:**

   `POST` /book <br />



###4. Update book

  Update 1 truyện

* **URL**

  /book/{book_id}

* **Method:**
  
  `PUT`
  
*  **Data Params**

   **Required:**
   
   ***
       {
           "title" : "Tên truyện",
           "content" : "Nội dung truyện"
       }

* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
    ***
        {
         "id" : 2,
         "title" : "Tên truyện",
         "content" : "Nội dung truyện"
        }
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:** 
    
        {
            "error" : true,
            "message" : "Duplicate content with book 2"
        }
    
      OR
    
        {
            "error" : true,
            "message" : "Invalid input data"
        }
       
      OR
    
        {
            "error" : true,
            "message" : "Can't update this book"
        }
        

* **Sample Call:**

   `PUT` /book/1 <br />



###5. Delete a specific book

  Xóa một truyện

* **URL**

  /book/{book_id}

* **Method:**
  
  `DELETE`
  
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
    ***
        {
            "error" : false,
            "message" : "Delete this book successful"
        }
        
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:** 
    
        {
            "error" : true,
            "message" : "This book doesn't exist" 
        }
        
      OR
    
        {
            "error" : true,
            "message" : "Can't delete this book"
        }
        

* **Sample Call:**

   `DELETE` /book/1 <br />


###6. Show of category of specific book

  Xem thông tin các thể loại của một truyện cụ thể

* **URL**

  /book/{book_id}/category

* **Method:**
  
  `GET`
  
*  **URL Params**

   **Optional:**
 
   `start=[0-~] unsigned integer default:0`
   
   `limit=[1-100] unsigned integer default:10`
   
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
    ***
        [
            {
                "name" : "Ngôn Tình",
                "id":1
            },
            {
                "name" : "Huyền Huyễn",
                "id" : 3
            }
        ]
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:** 
    
        {
            "error" : true, 
            "message" : "This book doesn't exist"
        }
    
      OR
    
        {
            "error" : true,
            "message" : "Empty data"
        }
        

* **Sample Call:**

   `GET` /book/1/category <br />
   `GET` /book/1/category?start=0&limit=2 <br />
   
   
##2. Category Node

###1. Get all category

  Lấy thông tin tất cả các thể loại

* **URL**

  /category

* **Method:**
  
  `GET`
  
*  **URL Params**

   **Optional:**
 
   `start=[0-~] unsigned integer default:0`
   
   `limit=[1-100] unsigned integer default:10`

* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
    ***
        [
            {
                "name" : "Ngôn Tình",
                "id" : 1
            },
            {
                "name" : "Xuyên Không",
                "id" : 2
            }
        ]
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:** 
    
    ***
    
        {
            "error" : true,
            "message" : "Empty data"
        }

* **Sample Call:**

   `GET` /category <br />
   `GET` /category?start=1&limit=2


###2. Get a specific category

  Lấy thông tin cụ thể của 1 thể loại

* **URL**

  /book/{category_id}

* **Method:**
  
  `GET`
  
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
    ***
        {
            "name" : "Ngôn Tình",
            "id" : 1
        }
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:**
    
    ***
        {
            "error" : true,
            "message" : "This category doesn't exist"
        }

* **Sample Call:**

   `GET` /category/1 <br />


###3. Insert category

  Thêm 1 thể loại

* **URL**

  /category

* **Method:**
  
  `POST`
  
*  **Data Params**

   **Required:**
   
   ***
       {
           "name" : "Tên thể loại"
       }

* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
    ***
        {
            "title" : "Tên thể loại",
            "id" : 2
        }
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
  
    **Content:** 
    
        {
            "error" : true,
            "message" : "Duplicate content with category 2"
        }
    
      OR
    
        {
            "error" : true,
            "message" : "Invalid input data"
        }
        
      OR
    
        {
            "error" : true,
            "message" : "Can't insert this category"
        }
        

* **Sample Call:**

   `POST` /category <br />



###4. Update category

  Update 1 thể loại

* **URL**

  /book/{category_id}

* **Method:**
  
  `PUT`
  
*  **Data Params**

   **Required:**
   
   ***
       {
           "name" : "Tên thể loại mới"
       }

* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
    ***
        {
         "id" : 2,
         "title" : "Tên thể loại mới"
        }
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:** 
    
        {
            "error" : true,
            "message" : "Duplicate content with category 2"
        }
    
      OR
    
        {
            "error" : true,
            "message" : "Invalid input data"
        }
        

* **Sample Call:**

   `PUT` /category/1 <br />



###5. Delete a specific category

  Xóa một thể loại

* **URL**

  /book/{category_id}

* **Method:**
  
  `DELETE`
  
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
    ***
        {
            "error" : false,
            "message" : "Delete this category successful"
        }
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:** 
    
        {
            "error" : true,
            "message" : "This category doesn't exist" 
        }
        
      OR
    
        {
            "error" : true,
            "message" : "Can't delete this category"
        }
        

* **Sample Call:**

   `DELETE` /category/1 <br />

###6. Show all book of specific category

  Xem danh sách các truyện thuộc một thể loại cụ thể

* **URL**

  /category/{category_id}/book

* **Method:**
  
  `GET`
  
*  **URL Params**

   **Optional:**
 
   `start=[0-~] unsigned integer default:0`
   
   `limit=[1-100] unsigned integer default:10`
   
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
    ***
        [
            {
                "id" : 29,
                "title" : "Giới Thần",
                "content" : "Nội dung truyện 29"
            },
            {
                "id" : 77,
                "title" : "Hạt Giống Tiến Hóa",
                "content" : "Nội dung truyện 77"
            }
        ]
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:** 
    
        {
            "error" : true, 
            "message" : "This category doesn't exist"
        }
    
      OR
    
        {
            "error" : true,
            "message" : "Empty data"
        }
        

* **Sample Call:**

   `GET` /category/1/book <br />
   `GET` /category/1/book?start=0&limit=2 <br />


##3. Book Category Node

###1. Get all book category

Lấy thông tin danh sách các mối liên kết giữa truyện và thể loại

* **URL**

  /bookcategory

* **Method:**
  
  `GET`
  
*  **URL Params**

   **Optional:**
 
   `start=[0-~] unsigned integer default:0`
   
   `limit=[1-100] unsigned integer default:10`

* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
    ***
        [
            {
                "category_id" : 1,
                "id" : 2,
                "book_id" : 1
            },
            {
                 "category_id" : 1,
                 "id" : 3,
                 "book_id" : 2
            }
        ]
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:** 
    
    ***
    
        {
            "error" : true,
            "message" : "Empty data"
        }

* **Sample Call:**

   `GET` /bookcategory <br />
   `GET` /bookcategory?start=1&limit=2

###2. Get a specific book category

  Lấy thông tin cụ thể của 1 liên hệ cụ thể giữa truyện và thể loại

* **URL**

  /bookcategory/{bookcategory_id}

* **Method:**
  
  `GET`
  
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
    ***
        {
            "category_id" : 2,
            "id" : 4,
            "book_id" : 4
        }
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:**
    
    ***
        {
            "error" : true,
            "message" : "This book category doesn't exist"
        }

* **Sample Call:**

   `GET` /bookcategory/1 <br />


###3. Insert book category

  Thêm 1 liên hệ giữa truyện và thể loại

* **URL**

  /bookcategory

* **Method:**
  
  `POST`
  
*  **Data Params**

   **Required:**
   
   ***
       {
           "category_id" : 2,
           "book_id" : 4
       }

* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
    ***
        {
            "category_id" : 2,
            "id" : 4,
            "book_id" : 4
        }
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
  
    **Content:** 
    
        {
            "error" : true,
            "message" : "Duplicate content with book category 2"
        }
    
      OR
    
        {
            "error" : true,
            "message" : "Invalid input data"
        }
        
      OR
    
        {
            "error" : true,
            "message" : "Can't insert this book category"
        }
        

* **Sample Call:**

   `POST` /bookcategory <br />

###4. Update book

  Update 1 liên hệ giữa truyện và thể loại

* **URL**

  /bookcategory/{bookcategory_id}

* **Method:**
  
  `PUT`
  
*  **Data Params**

   **Required:**
      
    ***
        {
            "category_id" : 2,
            "book_id" : 7
        }

* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
    ***
        {
            "category_id" : 2,
            "id" : 4,
            "book_id" : 7
        }
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:** 
    
        {
            "error" : true,
            "message" : "Duplicate content with book category 4"
        }
    
      OR
    
        {
            "error" : true,
            "message" : "Invalid input data"
        }
        

* **Sample Call:**

   `PUT` /bookcategory/1 <br />

###5. Delete a specific book category

  Xóa 1 liên hệ giữa truyện và thể loại

* **URL**

  /bookcategory/{bookcategory_id}

* **Method:**
  
  `DELETE`
  
* **Success Response:**
  
  * **Code:** 200 <br />
    **Content:** 
    
    ***
        {
            "error" : false,
            "message" : "Delete this book category successful"
        }
        
 
* **Error Response:**

  * **Code:** 400 Bad Request <br />
    **Content:** 
    
        {
            "error" : true,
            "message" : "This book category doesn't exist" 
        }
        
      OR
    
        {
            "error" : true,
            "message" : "Can't delete this book category"
        }
        

* **Sample Call:**

   `DELETE` /bookcategory/1 <br />
