import subprocess

# Which server to use:
serverPath = "http://127.0.0.1:8888"

# Which of the servelets to test:
apiPath = "/signup"

uSize = 20
mobSize = 100

fd = open("resultsignuptest1.txt", "w+")

for i in range(1, mobSize + 1):
    data = "username=user" + str(i % uSize + 1)  + "&email=user" + str(i % uSize + 1) + "@mail.com&password=asdfjkl;&repeatPassword=asdfjkl;"
    subprocess.Popen(["curl", "-s", "--data", data, serverPath+apiPath], stdout=fd)
