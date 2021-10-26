# Set the working environment
# usage: `. setup.sh`

# Java version
apt update
apt install openjdk-8-jdk -y

# Install mvn
VERSION=3.8.3
wget -O /tmp/maven.tar.gz https://dlcdn.apache.org/maven/maven-3/$VERSION/binaries/apache-maven-$VERSION-bin.tar.gz --no-check-certificate
tar -zxvf /tmp/maven.tar.gz -C /etc/
mv /etc/apache-maven-$VERSION /etc/maven

# Update linux PATH
export PATH=/etc/maven/bin:$PATH
echo PATH="$PATH" > /etc/environment
echo PATH="$PATH" >> /dockerstartup/generate_container_user
