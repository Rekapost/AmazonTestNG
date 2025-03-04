CI-CD
•	Build java app using maven
•	Sonarqube image scanning
•	Setup sonar server locally 
•	Send the report of our static code analysis to sonarqube
•	Build docker image, for this artifact we will push the docker image to docker hub
•	Using shell script directly update like in gitops approach we have to keep a manifest repo or create new folder inside source code repo     especially for the manifest to include the capabilities of git .
•	Instead of Image updater using shellscript automatically update manifest repo or similarly folder in  source  code repo is fine 
•	Using Argo CD, we will deploy this manifest automatically to k8s cluster 
•	Create EC2 instance with instance type large as no. of tools are heavy like sonar server, Jenkins server , docker  consume resource more than free tier instance

Prerequisites:
•	Java application code hosted on a Git repository
•	Jenkins server
•	Kubernetes cluster
•	Helm package manager
•	Argo CD
Steps:
1. Install the necessary Jenkins plugins:
   1.1 Git plugin
   1.2 Maven Integration plugin
   1.3 Pipeline plugin
   1.4 Kubernetes Continuous Deploy plugin

2. Create a new Jenkins pipeline:
   2.1 In Jenkins, create a new pipeline job and configure it with the Git repository URL for the Java application.
   2.2 Add a Jenkinsfile to the Git repository to define the pipeline stages.

3. Define the pipeline stages:
    Stage 1: Checkout the source code from Git.
    Stage 2: Build the Java application using Maven.
    Stage 3: Run unit tests using JUnit and Mockito.
    Stage 4: Run SonarQube analysis to check the code quality.
    Stage 5: Package the application into a JAR file.
    Stage 6: Deploy the application to a test environment using Helm.
    Stage 7: Run user acceptance tests on the deployed application.
    Stage 8: Promote the application to a production environment using Argo CD.

4. Configure Jenkins pipeline stages:
    Stage 1: Use the Git plugin to check out the source code from the Git repository.
    Stage 2: Use the Maven Integration plugin to build the Java application.
    Stage 3: Use the JUnit and Mockito plugins to run unit tests.
    Stage 4: Use the SonarQube plugin to analyze the code quality of the Java application.
    Stage 5: Use the Maven Integration plugin to package the application into a JAR file.
    Stage 6: Use the Kubernetes Continuous Deploy plugin to deploy the application to a test environment using Helm.
    Stage 7: Use a testing framework like Selenium to run user acceptance tests on the deployed application.
    Stage 8: Use Argo CD to promote the application to a production environment.

5. Set up Argo CD:
    Install Argo CD on the Kubernetes cluster.
    Set up a Git repository for Argo CD to track the changes in the Helm charts and Kubernetes manifests.
    Create a Helm chart for the Java application that includes the Kubernetes manifests and Helm values.
    Add the Helm chart to the Git repository that Argo CD is tracking.

6. Configure Jenkins pipeline to integrate with Argo CD:
   6.1 Add the Argo CD API token to Jenkins credentials.
   6.2 Update the Jenkins pipeline to include the Argo CD deployment stage.

7. Run the Jenkins pipeline:
   7.1 Trigger the Jenkins pipeline to start the CI/CD process for the Java application.
   7.2 Monitor the pipeline stages and fix any issues that arise.

PS C:\Users\nreka\vscodedevops\amazon>  mvn clean install -U
[INFO] Installing C:\Users\nreka\vscodedevops\amazon\target\amazon-1.0-SNAPSHOT.jar to C:\Users\nreka\.m2\repository\amazon\amazon\1.0-SNAPSHOT\amazon-1.0-SNAPSHOT.jar


This will show the output of the running Maven test execution.
docker logs -f amazon-testng-container


reka@Reka:/mnt/c/Users/nreka/vscodedevops/amazon$ ls -l ~/amazon.pem
-r-------- 1 reka reka 1674 Mar  1 18:50 /home/reka/amazon.pem

reka@Reka:/mnt/c/Users/nreka/vscodedevops/amazon$ ssh -i ~/amazon.pem ubuntu@44.201.2.251
ubuntu@ip-172-31-0-44:~$ sudo apt update
                         sudo apt upgrade
                         sudo apt install openjdk-11-jre
                         java -version

### Install jenkins :
curl -fsSL https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key | sudo tee \  
 /usr/share/keyrings/jenkins-keyring.asc > /dev/null

sudo wget -O /usr/share/keyrings/jenkins-keyring.asc \
    https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key


echo "deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc]" \
    https://pkg.jenkins.io/debian-stable binary/ | sudo tee \
    /etc/apt/sources.list.d/jenkins.list > /dev/null

sudo apt-get update
sudo apt-get install fontconfig openjdk-17-jre

sudo apt-get install jenkins
sudo cat /etc/apt/sources.list.d/jenkins.list
ls /etc/apt/sources.list.d/
sudo apt update
sudo apt install jenkins -y

Start Jenkins :
sudo systemctl start jenkins
sudo systemctl enable jenkins
sudo systemctl status jenkins
 
 http://44.201.2.251:8080/
 Jenkins is up and running :
 ![alt text](image-2.png)
 ![alt text](image.png)

Jenkins is running in EC2 INstance:
 ![alt text](image.png)
 
 - Create Pipeline Project
 - Install docker pipeline plugin and SonarQube Scanner 
 ![alt text](image.png)

 Go to ec2 instance and install sonar server
 Configure a Sonar Server locally
 Add user called sonarqube 
 Go to root user

ubuntu@ip-172-31-0-44:~$ sudo su -
root@ip-172-31-0-44:~# apt install unzip
adduser sonarqube

switch to user : 
root@ip-172-31-0-44:~# sudo su - sonarqube 
sonarqube@ip-172-31-0-44:~$ wget https://binaries.sonarsource.com/Distribution/sonarqube/sonarqube-9.4.0.54424.zip
 unzip *
sonarqube@ip-172-31-0-44:~$ ls 
sonarqube-9.4.0.54424  sonarqube-9.4.0.54424.zip
chmod -R 755 /home/sonarqube/sonarqube-9.4.0.54424
chown -R sonarqube:sonarqube /home/sonarqube/sonarqube-9.4.0.54424
sonarqube@ip-172-31-0-44:~$ cd sonarqube-9.4.0.54424/bin/
sonarqube@ip-172-31-0-44:~/sonarqube-9.4.0.54424/bin$ ls
jsw-license  linux-x86-64  macosx-universal-64  windows-x86-64
sonarqube@ip-172-31-0-44:~/sonarqube-9.4.0.54424/bin$ cd ~/sonarqube-9.4.0.54424/bin/windows-x86-64
sonarqube@ip-172-31-0-44:~/sonarqube-9.4.0.54424/bin/windows-x86-64$ 
sonarqube@ip-172-31-0-44:~/sonarqube-9.4.0.54424/bin/windows-x86-64$ cd ~/sonarqube-9.4.0.54424/bin/linux-x86-64
sonarqube@ip-172-31-0-44:~/sonarqube-9.4.0.54424/bin/linux-x86-64$
sonarqube@ip-172-31-0-44:~/sonarqube-9.4.0.54424/bin/linux-x86-64$ ./sonar.sh start
Starting SonarQube...
Started SonarQube.
Server on http://<ec2ip-address>:9000
   Username: admin
   Password: admin


Docker Slave Configuration
Install docker 
•	Run the command below to Install Docker
root@ip-172-31-0-237:~# sudo apt update
root@ip-172-31-0-237:~# sudo apt install docker.io
Grant Jenkins user and Ubuntu user permission to docker deamon.
root@ip-172-31-0-237:~# sudo su -
root@ip-172-31-0-237:~# usermod -aG docker jenkins
root@ip-172-31-0-237:~# usermod -aG docker ubuntu
root@ip-172-31-0-237:~# systemctl restart docker
 Once you are done with the above steps, it is better to restart Jenkins.
•	http://<ec2-instance-public-ip>:8080/restart
•	The docker agent configuration is now successful.

ArgoCD and K8s 
![alt text](image.png)

curl -sL https://github.com/operator-framework/operator-lifecycle-manager/releases/download/v0.31.0/install.sh | bash -s v0.31.0

kubectl create -f https://operatorhub.io/install/argocd-operator.yaml
kubectl get pods -n operators
![alt text](image.png)
![alt text](image.png)
reka@Reka:/mnt/c/Users/nreka/vscodedevops/amazon$ kubectl get pods -n operators
NAME                                                  READY   STATUS              RESTARTS   AGE
argocd-operator-controller-manager-55c5c6867c-zb55g   0/1     ContainerCreating   0          10s
reka@Reka:/mnt/c/Users/nreka/vscodedevops/amazon$ kubectl get pods -n operators
NAME                                                  READY   STATUS    RESTARTS   AGE
argocd-operator-controller-manager-55c5c6867c-zb55g   1/1     Running   0          70s
reka@Reka:/mnt/c/Users/nreka/vscodedevops/amazon$ kubectl get csv -n operators
NAME                      DISPLAY   VERSION   REPLACES                  PHASE
argocd-operator.v0.13.0   Argo CD   0.13.0    argocd-operator.v0.12.0   Succeeded


docker build -t reka83/amazon-testng:latest .
docker push reka83/amazon-testng:latest
docker pull reka83/amazon-testng:latest


reka@Reka:/mnt/c/Users/nreka/vscodedevops/amazon$ minikube status
minikube
type: Control Plane
host: Running
kubelet: Running
apiserver: Running
kubeconfig: Configured


kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

kubectl get crds | grep argoproj





reka@Reka:/mnt/c/Users/nreka/vscodedevops/amazon$ kubectl get svc -n argocd
NAME                                      TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)                      AGE
argocd-applicationset-controller          ClusterIP   10.97.136.154    <none>        7000/TCP,8080/TCP            21m
argocd-dex-server                         ClusterIP   10.97.227.67     <none>        5556/TCP,5557/TCP,5558/TCP   21m
argocd-metrics                            ClusterIP   10.100.224.214   <none>        8082/TCP                     21m
argocd-notifications-controller-metrics   ClusterIP   10.96.198.44     <none>        9001/TCP                     21m
argocd-redis                              ClusterIP   10.100.171.253   <none>        6379/TCP                     21m
argocd-repo-server                        ClusterIP   10.96.77.168     <none>        8081/TCP,8084/TCP            21m
argocd-server                             ClusterIP   10.109.112.86    <none>        80/TCP,443/TCP               21m
argocd-server-metrics                     ClusterIP   10.98.97.133     <none>        8083/TCP                     21m

argocd-server                             NodePort    10.109.112.86    <none>        80:31959/TCP,443:30623/TCP   23m
argocd-server-metrics                     ClusterIP   10.98.97.133     <none>        8083/TCP                     23m
reka@Reka:/mnt/c/Users/nreka/vscodedevops/amazonreka@Reka:/mnt/c/Users/nreka/reka@Reka:/mnt/c/Users/nreka/vscodedevops/amareka@Reka:/mnt/c/Users/nreka/vscreka@Reka:/mnt/c/Users/nreka/vscodedevops/amazon$ minikube ip
192.168.58.2
reka@Reka:/mnt/c/Users/nreka/vscodedevops/amazon$ kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 --decode