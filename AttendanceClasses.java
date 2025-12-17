// Abstract class demonstrating inheritance and abstract methods
abstract class Person {
    protected String name;
    protected String id;
    
    public Person(String name, String id) {
        this.name = name;
        this.id = id;
    }
    
    // Abstract method that must be implemented by subclasses
    public abstract String getRole();
    
    // Concrete methods
    public String getName() {
        return name;
    }
    
    public String getId() {
        return id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}

// Concrete class extending abstract class
class Student extends Person {
    private int totalClasses;
    private int attendedClasses;
    
    public Student(String name, String id) {
        super(name, id);
        this.totalClasses = 0;
        this.attendedClasses = 0;
    }
    
    @Override
    public String getRole() {
        return "Student";
    }
    
    public int getTotalClasses() {
        return totalClasses;
    }
    
    public int getAttendedClasses() {
        return attendedClasses;
    }
    
    public void setTotalClasses(int totalClasses) {
        this.totalClasses = totalClasses;
    }
    
    public void setAttendedClasses(int attendedClasses) {
        this.attendedClasses = attendedClasses;
    }
}

// Interface defining contract for attendance calculations
interface AttendanceCalculator {
    double calculateAttendancePercentage(Student student);
    boolean isEligibleForExam(Student student, double minimumPercentage);
    int getRequiredClassesToAttend(Student student, double targetPercentage);
    String getAttendanceStatus(Student student);
}

// Interface for data validation
interface DataValidator {
    boolean validateStudentData(String name, String id, int totalClasses, int attendedClasses);
    String getValidationMessage();
}

// Concrete implementation of interfaces
class AttendanceManager implements AttendanceCalculator, DataValidator {
    private String validationMessage;
    
    @Override
    public double calculateAttendancePercentage(Student student) {
        if (student.getTotalClasses() == 0) {
            return 0.0;
        }
        return (double) student.getAttendedClasses() / student.getTotalClasses() * 100;
    }
    
    @Override
    public boolean isEligibleForExam(Student student, double minimumPercentage) {
        return calculateAttendancePercentage(student) >= minimumPercentage;
    }
    
    @Override
    public int getRequiredClassesToAttend(Student student, double targetPercentage) {
        int totalClasses = student.getTotalClasses();
        int attendedClasses = student.getAttendedClasses();
        
        if (totalClasses == 0) {
            return 0;
        }
        
        double currentPercentage = calculateAttendancePercentage(student);
        if (currentPercentage >= targetPercentage) {
            return 0;
        }
        
        // Calculate additional classes needed
        double numerator = targetPercentage * totalClasses - 100 * attendedClasses;
        double denominator = 100 - targetPercentage;
        
        if (denominator <= 0) {
            return -1; // Target percentage not achievable
        }
        
        return (int) Math.ceil(numerator / denominator);
    }
    
    @Override
    public String getAttendanceStatus(Student student) {
        double percentage = calculateAttendancePercentage(student);
        
        if (percentage >= 90) {
            return "Excellent";
        } else if (percentage >= 75) {
            return "Good";
        } else if (percentage >= 60) {
            return "Average";
        } else if (percentage >= 40) {
            return "Below Average";
        } else {
            return "Poor";
        }
    }
    
    @Override
    public boolean validateStudentData(String name, String id, int totalClasses, int attendedClasses) {
        if (name == null || name.trim().isEmpty()) {
            validationMessage = "Name cannot be empty";
            return false;
        }
        
        if (id == null || id.trim().isEmpty()) {
            validationMessage = "ID cannot be empty";
            return false;
        }
        
        if (totalClasses < 0) {
            validationMessage = "Total classes cannot be negative";
            return false;
        }
        
        if (attendedClasses < 0) {
            validationMessage = "Attended classes cannot be negative";
            return false;
        }
        
        if (attendedClasses > totalClasses) {
            validationMessage = "Attended classes cannot exceed total classes";
            return false;
        }
        
        validationMessage = "Valid data";
        return true;
    }
    
    @Override
    public String getValidationMessage() {
        return validationMessage;
    }
}
