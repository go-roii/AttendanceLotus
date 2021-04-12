# AttendanceLotus
AttendanceLotus is an attendance system capable of taking attendance with multiple classes.
## Features
- Simple UI
- Search students or teachers
#### Admin
- Add students and teachers
- Add courses, classes, and subjects

![Admin user - Students](readme-assets/admin/admin_students.png)
![Admin user - Teachers](readme-assets/admin/admin_teachers.png)
![Admin user - Curriculums](readme-assets/admin/admin_curriculums.png)

#### Teacher
- Take attendance
- View attendance
- Add students to classes
- Add classes

![Teacher user - Attendance](readme-assets/teacher/teacher_attendance.png)
![Teacher user - Students](readme-assets/teacher/teacher_students.png)
![Teacher user - Classes](readme-assets/teacher/teacher_classes.png)
## Setup
1. Start MySQL and Apache in XAMPP
2. Open <https://localhost/phpmyadmin>
3. Create new database named `attendancelotus`
4. Click **Import**
5. Locate `AttendanceLotus/db/attendanceLotus.sql` then **Click File** or drag it to the window 
## Usage
#### Admin
- `username:` admin
- `password:` admin
#### Teacher
You can check on **Admin > Teachers**

**All lowercase**

First name initial + Middle initial + Last name + number

Example: Richard Flores Gopez
- `username:` rfgopez1
- `password:` 1234
