package priv.adt.interval.DutyRoster;

/**
 * 表示一个职员以及他的工作时间等信息
 */
public class Employee {
    private final String name;
    private final String position;
    private final String phone;

    public Employee(String name, String position, String phone) {
        this.name = name;
        this.position = position;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return name + " " + position + " " + phone;
    }

    public String get_phone() {
        return phone;
    }

    public String get_name() {
        return name;
    }

    public String get_position() {
        return position;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        else if (obj == null || getClass() != obj.getClass())
            return false;
        else {
            Employee employee = (Employee) obj;
            return name.equals(employee.name) && position.equals(employee.position) && phone.equals(employee.phone);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;

        int length = Math.min(Math.min(name.length(), position.length()), phone.length());
        for (int i = 0; i < length; i++) {
            hash = hash * 31 + name.charAt(i) + position.charAt(i) + phone.charAt(i);

        }
        return hash;
    }
}
