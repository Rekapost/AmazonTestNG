
![alt text](image.png)
![alt text](image-3.png)
![alt text](image-1.png)
![alt text](image-2.png)

## To increase no of nodes 
docker-compose scale chrome=3
docker-compose up -d --scale firefox=3
mvn clean test
![alt text](image-4.png)
![alt text](image-5.png)
![alt text](image-6.png)
![alt text](image-7.png)
Scale Down Chrome Nodes to 1:
docker-compose up --scale chrome=1

Scale Down Edge Nodes to 0 (effectively stop all Edge nodes):
docker-compose up --scale edge=0

Scale Down Firefox Nodes to 0:
docker-compose up --scale firefox=0

![alt text](image-8.png)
