async function saveAsPDF(firstName, lastName) {
    const { jsPDF } = jspdf

    const doc = new jsPDF()

    await doc.text("Grades: " + firstName + " " + lastName.toUpperCase(), doc.internal.pageSize.width / 2, 30, null, null, "center")

    doc.autoTable({
        html: "#grades_container",
        startY: 60,
        align: "center"
    })

    doc.save("grades_" + firstName + "_" + lastName.toUpperCase() + ".pdf")
}