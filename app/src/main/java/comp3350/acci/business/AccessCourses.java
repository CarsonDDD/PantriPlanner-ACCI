package comp3350.acci.business;

import java.util.Collections;
import java.util.List;

import comp3350.acci.application.Services;
import comp3350.acci.objects.Course;
import comp3350.acci.objects.persistence.CoursePersistence;

public class AccessCourses
{
	private CoursePersistence coursePersistence;
	private List<Course> courses;
	private Course course;
	private int currentCourse;

	public AccessCourses()
	{
		coursePersistence = Services.getCoursePersistence();
		courses = null;
		course = null;
		currentCourse = 0;
	}

    public List<Course> getCourses()
    {
        courses = coursePersistence.getCourseSequential();
        return Collections.unmodifiableList(courses);
    }

	public Course getSequential()
	{
		String result = null;
		if (courses == null)
		{
            courses = coursePersistence.getCourseSequential();
			currentCourse = 0;
		}
		if (currentCourse < courses.size())
		{
			course = (Course) courses.get(currentCourse);
			currentCourse++;
		}
		else
		{
			courses = null;
			course = null;
			currentCourse = 0;
		}
		return course;
	}

	public Course getRandom(String courseID)
	{
		courses = coursePersistence.getCourseRandom(new Course(courseID));
		currentCourse = 0;
		if (currentCourse < courses.size())
		{
			course = courses.get(currentCourse);
			currentCourse++;
		}
		else
		{
			courses = null;
			course = null;
			currentCourse = 0;
		}
		return course;
	}

	public Course insertCourse(Course currentCourse)
	{
		return coursePersistence.insertCourse(currentCourse);
	}

	public Course updateCourse(Course currentCourse)
	{
		return coursePersistence.updateCourse(currentCourse);
	}

	public void deleteCourse(Course currentCourse)
	{
		coursePersistence.deleteCourse(currentCourse);
	}

}
