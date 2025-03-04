provider "aws" {
  region = "us-east-1" # Replace with your desired AWS region.
}

variable "cidr" {
  default = "10.0.0.0/16"
}

resource "aws_key_pair" "example" {
  key_name   = "terraform-amazon-maven" # Replace with your desired key name
  #public_key = file("${path.module}/amazon_key.pub") # Replace with the path to your public key file
  public_key = file("/home/reka/amazon_key.pub")  # Use the absolute path
}

resource "aws_vpc" "myvpc" {
  cidr_block = var.cidr
}

resource "aws_subnet" "sub1" {
  vpc_id                  = aws_vpc.myvpc.id
  cidr_block              = "10.0.0.0/24"
  availability_zone       = "us-east-1a"
  map_public_ip_on_launch = true
}

resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.myvpc.id
}

resource "aws_route_table" "RT" {
  vpc_id = aws_vpc.myvpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }
}

resource "aws_route_table_association" "rta1" {
  subnet_id      = aws_subnet.sub1.id
  route_table_id = aws_route_table.RT.id
}

resource "aws_security_group" "webSg" {
  name   = "web"
  vpc_id = aws_vpc.myvpc.id

  ingress {
    description = "HTTP from VPC"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    description = "SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
  description = "App Port"
  from_port   = 4000
  to_port     = 4000
  protocol    = "tcp"
  cidr_blocks = ["0.0.0.0/0"]
    }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "Web-sg"
  }
}

resource "aws_instance" "server" {
  ami                    = "ami-0866a3c8686eaeeba"
  instance_type          = "t2.micro"
  key_name               = aws_key_pair.example.key_name
  vpc_security_group_ids = [aws_security_group.webSg.id]
  subnet_id              = aws_subnet.sub1.id
  iam_instance_profile   = aws_iam_instance_profile.iam_instance_profile.name  # Attach IAM role
  connection {
    type        = "ssh"
    user        = "ubuntu"              # Replace with the appropriate username for your EC2 instance
    #private_key = file("${path.module}/amazon_key") # Replace with the path to your private key
    private_key = file("/home/reka/amazon_key") # Use the absolute path
    host        = self.public_ip
  }

  user_data = <<-EOF
                #!/bin/bash
                apt update -y
                apt install -y openjdk-11-jdk maven
              EOF

  tags = {
    Name = "AmazonEC2Instance"
  }

  provisioner "file" {
    source      = "/mnt/c/Users/nreka/vscodedevops/amazon/terraform" # This path needs to be correct
    destination = "/home/ubuntu/amazon"
  }

  provisioner "remote-exec" {
    inline = [
      "echo 'Updating System and Installing Java and Maven...'",
      "sudo apt update -y",
      "sudo apt install -y wget unzip",
      "sudo apt install -y openjdk-17-jdk maven", # Install Java and Maven
      "sudo apt --fix-broken install -y",
      "echo 'Installing Google Chrome...' ",
      "wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb",
      "sudo dpkg -i google-chrome-stable_current_amd64.deb || sudo apt --fix-broken install -y",
      "google-chrome --version",  # Verify Chrome installation

      #"sudo mkdir -p /home/ubuntu/TestNG Azure",  # Create the project directory if it doesn't exist
      "echo 'Cloning AmazonTestNG repository...'",
      "cd /home/ubuntu", # Navigate to the project folder
       "if [ ! -d AmazonTestNG ]; then git clone https://github.com/Rekapost/AmazonTestNG.git; fi",  # Ensure repo is cloned
      #"git clone https://github.com/Rekapost/AmazonTestNG || echo 'Repo already exists'",  # Optionally clone your Maven project (replace with actual repository URL)
      
      "cd AmazonTestNG",

      "echo 'Downloading ChromeDriver...' ",
      "wget https://storage.googleapis.com/chrome-for-testing-public/133.0.6943.141/linux64/chromedriver-linux64.zip",
      "unzip chromedriver-linux64.zip",
      #"sudo mv chromedriver-linux64/chromedriver /usr/local/bin/",
      "sudo mv chromedriver-linux64/chromedriver /usr/bin/chromedriver",
      "sudo chmod +x /usr/bin/chromedriver",
      "echo 'export PATH=$PATH:/usr/bin' >> ~/.bashrc",
      "source ~/.bashrc",
      "chromedriver --version",  # Verify ChromeDriver installation
      
      "echo 'Running Maven build and tests...'",
      "mvn clean install",
      "mvn test",    # Build the Maven project, Runs TestNG tests instead of expecting a JAR file
      
      "echo 'Checking if surefire-reports directory exists...' ",
      "if [ -d target/surefire-reports ]; then zip -r target/surefire-reports.zip target/surefire-reports; fi",

      "echo 'Maven build and tests completed!'"
    ]
  }
}

resource "random_id" "bucket_id" {
  byte_length = 4
}

# Create an S3 bucket to store Maven test results
resource "aws_s3_bucket" "maven_results" {
  #bucket = "amazon-maven-test-results-${random_id.bucket_id.hex}" # Specify your S3 bucket name
    bucket = "amazon-maven-test-results"
}

resource "aws_s3_bucket_public_access_block" "allow_public" {
  bucket = aws_s3_bucket.maven_results.id

  block_public_acls       = false
  block_public_policy     = false
  ignore_public_acls      = false
  restrict_public_buckets = false
  depends_on = [aws_s3_bucket.maven_results]  # Ensures bucket exists first
}

# Upload the results to the S3 bucket using aws_s3_object
/*resource "aws_s3_object" "results" {
  bucket = aws_s3_bucket.maven_results.bucket
  key    = "test_results/surefire-reports.zip"  # S3 file path
  source = "/home/ubuntu/target-maven_project/target/surefire-reports.zip"  # Path to the file you want to upload
  acl    = "private"  # Optional: Set the access control list for the file
}
*/
/*
resource "null_resource" "maven_build" {
  provisioner "remote-exec" {
    inline = [
      "cd /home/ubuntu/target-maven_project",
      "mvn clean install"
    ]
  }
}
*/

### Create IAM policy
resource "aws_iam_policy" "ec2_s3_access_policy" {
  name        = "ec2_s3_access_policy"
  description = "Permissions for EC2 to access S3 bucket"
  policy = jsonencode({
    Version : "2012-10-17",
    Statement : [
      {
        Effect : "Allow",
        Action : [
          "s3:PutObject",
          "s3:GetObject",
          "s3:ListBucket"
        ],
        Resource : [
          "arn:aws:s3:::amazon-maven-test-results",
          "arn:aws:s3:::amazon-maven-test-results/*"
        ]
      }
    ]
  })
}

resource "aws_s3_bucket_policy" "maven_results_policy" {
  bucket = aws_s3_bucket.maven_results.id

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Principal = "*",
        #Principal = {
        #  AWS = "arn:aws:iam::876716894925:user/terraform"
        #},
        Action = [
          #"s3:PutObject",
          "s3:GetObject"
          #"s3:ListBucket",
          #"s3:PutBucketPolicy"
        ],
        Resource = [
          #"arn:aws:s3:::amazon-maven-test-results",
          "arn:aws:s3:::amazon-maven-test-results/*"
        ]
      }
    ]
  })
}

### Create IAM role
resource "aws_iam_role" "ec2_to_s3_role" {
  name = "ec2_to_s3_role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Sid    = "EC2ToS3Access"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      },
    ]
  })
}

### Attach IAM policy to IAM role
resource "aws_iam_policy_attachment" "policy_attach" {
  name       = "example_policy_attachment"
  roles      = [aws_iam_role.ec2_to_s3_role.name]
  policy_arn = aws_iam_policy.ec2_s3_access_policy.arn
}

### Create instance profile using role
resource "aws_iam_instance_profile" "iam_instance_profile" {
  name = "iam_instance_profile"
  role = aws_iam_role.ec2_to_s3_role.name
}

resource "aws_s3_object" "results" {
  #depends_on = [null_resource.maven_build]  # Ensure build finishes before upload
  bucket = aws_s3_bucket.maven_results.id
  key    = "surefire-reports.zip"
  #source = "/home/ubuntu/AmazonTestNG/target/surefire-reports.zip"
  #source = "${path.module}//target//surefire-reports.zip"
  #source = "C:/Users/nreka/vscodedevops/amazon/target/surefire-reports.zip"
  source = "/mnt/c/Users/nreka/vscodedevops/amazon/target/surefire-reports.zip"
  #acl = "private"
  acl    = "public-read"   # Change to 'public-read'
}
