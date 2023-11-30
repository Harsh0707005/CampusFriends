# CampusFriends

This is a working prototype of the app, and designing it will make it look even better.

The app is integrated with Firebase to make all the data dynamic and easy to modify
# Firebase Structure
### Firebase Authentication
<pre>Phone Number used for authentication<br></pre>
Used for managing authentication details of each user (with unique uid of each user)

### Realtime Database:
<pre>
-Users
   -uid1
      -user data
      -Contacts
         -uid
   -uid2
       -user data
       -Contacts
         -uid  
  ...
-World Chat
    -uid1
        -message data
        -time
    -uid2
        -message data
        -time
 ...
</pre>

### Firebase Storage

<pre>
-Course Images
    -images for each course
-Mock
    -excel files for each mock tests
-PDF
    -pdf documents for each course
-Videos
    -videos for each course
</pre>

### Firebase Database
<pre>
-Users <b>(automatically managed by app)</b>
    -uid1
        -user data
    -uid2
        -user data
-Universities
    -university1
        -Groups
            -group1
                -Semesters
                    -SEMESTER 1
                        -Courses
                            -Course 1
                                -course data (name, image, etc)
                                -PDF
                                    -pdf1
                                        -name of pdf
                                    -pdf2
                                        -name of pdf
                                    ...
                              -Course 2
                                -course data (name, image, etc)
                                -PDF
                                    -pdf1
                                        -name of pdf
                                    -pdf2
                                        -name of pdf
                                    ...
                              ...
  
                    -SEMESTER 2
                        -Courses
                            -Course 1
                                -course data (name, image, etc)
                                -PDF
                                    -pdf1
                                        -name of pdf
                                    -pdf2
                                        -name of pdf
                                    ...
                              -Course 2
                                -course data (name, image, etc)
                                -PDF
                                    -pdf1
                                        -name of pdf
                                    -pdf2
                                        -name of pdf
                                    ...
                              ...
                    ...
  
            -group2
                -Semesters
                    -SEMESTER 1
                        -Courses
                            -Course 1
                                -course data (name, image, etc)
                                -PDF
                                    -pdf1
                                        -name of pdf
                                    -pdf2
                                        -name of pdf
                                    ...
                              -Course 2
                                -course data (name, image, etc)
                                -PDF
                                    -pdf1
                                        -name of pdf
                                    -pdf2
                                        -name of pdf
                                    ...
                              ...
  
                    -SEMESTER 2
                        -Courses
                            -Course 1
                                -course data (name, image, etc)
                                -PDF
                                    -pdf1
                                        -name of pdf
                                    -pdf2
                                        -name of pdf
                                    ...
                              -Course 2
                                -course data (name, image, etc)
                                -PDF
                                    -pdf1
                                        -name of pdf
                                    -pdf2
                                        -name of pdf
                                    ...
                              ...
                    ...
            ...
    ...
-Mock Tests
    -test1
        -name of mock1
    -test2
        -name of mock2
</pre>
https://github.com/Harsh0707005/CampusFriends/assets/80234396/d702aea9-f527-473d-98cd-8cdfcd9cd9cb

