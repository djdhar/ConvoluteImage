# Let's Convolute an Image

Basic funda is creating 4 images for a single image

* Suppose we get the following image
* ``` 
  01 02 03 04
  05 06 07 08
  09 10 11 12
  13 14 15 16
  ```
* In the first step we convert the image into following two images
* ``` 
  Image A = 
  01 02 03 04
  09 10 11 12
  ```
* ``` 
  Image B =
  05 06 07 08
  13 14 15 16
  ```
* Image A is further split into two images
* ``` 
  Image A1 = 
  01 03
  09 11
  ```
* ``` 
  Image A2 = 
  02 04
  10 12
  ```
* Image B is further split into two images
* ``` 
  Image B1 = 
  05 07
  13 15
  ```
* ``` 
  Image B2 = 
  06 08
  14 16
  ```
* Basically as the final result all the four images are resized to `200 X 200` size and then they are combined and sent to API response.
  

### API Struture

## Get list of Things

### Request

`POST /convImage`

    curl --location --request POST 'localhost:8069/convImage' \
    --header 'Content-type: multipart/form-data' \
    --form 'image=@"/C:/Users/HP/Pictures/myImage.PNG"'

### Request Image
![Sample.jpeg](..%2F..%2FSample.jpeg)
### Response Image
![response.jpeg](..%2F..%2Fresponse.jpeg)


