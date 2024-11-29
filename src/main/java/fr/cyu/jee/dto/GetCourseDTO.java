package fr.cyu.jee.dto;

import java.time.LocalDate;
import java.util.Optional;

public class GetCourseDTO {

    private LocalDate date;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Optional<LocalDate> getDateOptional() {
        return Optional.ofNullable(date);
    }
}
