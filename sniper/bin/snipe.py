import requests
from bs4 import BeautifulSoup
import time

# Replace with the URL of the course page you want to monitor
COURSE_URL = "https://sims.rutgers.edu/csp/builder.htm?semester=92024#Page_CSPSelectSectionTab"

# Replace with your actual username and password
USERNAME = "psc84"
PASSWORD = "Cpulin@001"

def check_course_availability():
    try:
        # Create a session with your credentials
        session = requests.Session()
        login_url = "https://sims.rutgers.edu/csp/builder.htm?semester=92024#Page_CSPSelectSectionTab"  # Replace with the actual login URL
        login_data = {"username": USERNAME, "password": PASSWORD}
        session.post(login_url, data=login_data)
        TARGET_CRN= "09650"
        # Now make a request to the protected page
        response = session.get(COURSE_URL)
        soup = BeautifulSoup(response.content, "html.parser")
        course_elements = soup.find_all("tr", class_="course-offering")

        for course in course_elements:
            crn = course.find("td", class_="crn").text.strip()
            if crn == TARGET_CRN:
                status = course.find("td", class_="status").text.strip()
                if status == "Open":
                    print(f"Course {TARGET_CRN} is now OPEN! Register quickly!")
                    return True
                else:
                    print(f"Course {TARGET_CRN} status: {status}")
                    return False

        print(f"Course {TARGET_CRN} not found on the page.")
        return False

    except Exception as e:
        print(f"Error checking course availability: {e}")
        return False

def main():
    while True:
        if check_course_availability():
            break
        time.sleep(10)  # Adjust the interval as needed

if __name__ == "__main__":
    main()
