function updateUserType() {
    const selectedType = document.getElementById("userType").value
    const selectSubject = document.getElementById("subjectDiv")
    selectSubject.style.visibility = selectedType === "TEACHER" ? "visible" : "hidden"
}