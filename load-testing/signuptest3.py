import subprocess

# Which server to use:
serverPath = "http://127.0.0.1:8888"

# Which of the servelets to test:
apiPath = "/signup"

uSize = 20
mobSize = 100

fd = open("resultsignuptest3.txt", "w+")

for i in range(1, uSize + 1):
    for j in range(1, (mobSize / uSize) + 1):
        data = "username=user" + str(i) + "&email=user" + str(i) + "@mail.com&password=asdfjkl;&repeatPassword=asdfjkl;"
        subprocess.Popen(["curl", "-s", "--data", data, serverPath+apiPath], stdout=fd)
