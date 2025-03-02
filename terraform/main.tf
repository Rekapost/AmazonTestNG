provider "aws" {
  region = "us-east-1" # Replace with your desired AWS region.
}

variable "cidr" {
  default = "10.0.0.0/16"
}

resource "aws_key_pair" "example" {
  key_name   = "terraform-amazon-maven" # Replace with your desired key name
  public_key = file("~/.ssh/id_rsa.pub") # Replace with the path to your public key file
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

  connection {
    type        = "ssh"
    user        = "ubuntu"              # Replace with the appropriate username for your EC2 instance
    private_key = file("~/.ssh/id_rsa") # Replace with the path to your private key
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
    source      = "/mnt/c/Users/nreka/vscodedevops/amazon" # This path needs to be correct
    destination = "/home/ubuntu/terraform-amazon-maven"
  }


  provisioner "remote-exec" {
    inline = [
      "echo 'Installing Java and Maven...'",
      "sudo apt update -y",
      "sudo apt install -y openjdk-11-jdk maven", # Install Java and Maven
      #"sudo mkdir -p /home/ubuntu/TestNG Azure",  # Create the project directory if it doesn't exist
      "cd /home/ubuntu/amazon", # Navigate to the project folder
      "git clone https://github.com/Rekapost/AmazonTestNG",  # Optionally clone your Maven project (replace with actual repository URL)
      #"cd /home/ubuntu/terraform-maven_project/terraform-maven_project-",  # Navigate to the cloned project folder
      "mvn clean install",                                     # Build the Maven project
      "java -jar target/AmazonTestNG-0.0.1-SNAPSHOT.jar &" # Run the JAR (replace with your actual JAR name)
    ]
  }
}

# Create an S3 bucket to store Maven test results
resource "aws_s3_bucket" "maven_results" {
  bucket = "my-maven-test-results" # Specify your S3 bucket name
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
  description = "Permissions / Access for EC2 and S3"
  policy = jsonencode({
    Version : "2012-10-17",
    Statement : [
      {
        Action : "ec2:*",
        Effect : "Allow",
        Resource : "*"
      },
      {
        Action : "s3:*",
        Effect : "Allow",
        Resource : "*"
      },
      {
        "Effect" : "Allow",
        "Action" : [
          "s3:PutObject",
          "s3:GetObject",
          "s3:ListBucket"
        ],
        "Resource" : [
          "arn:aws:s3:::my-maven-test-results",
          "arn:aws:s3:::my-maven-test-results/*"
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
  bucket = aws_s3_bucket.maven_results.bucket
  key    = "test_results/surefire-reports.zip"
  source     = "/home/ubuntu/TestNG-Azure/target/surefire-reports.zip"
  acl = "private"
}


