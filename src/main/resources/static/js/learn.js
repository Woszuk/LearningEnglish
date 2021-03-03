window.addEventListener("DOMContentLoaded", function () {
    let field = document.querySelectorAll(".learn__field");
    let hidden = document.querySelector("#hidden").value;
    let form = document.querySelector("form");
    let check = document.querySelector("#check");

    if (sessionStorage.getItem("correctAnswer") === null) {
        sessionStorage.setItem("correctAnswer", 0);
    }

    for(let i=0; i< field.length; i++) {
        field[i].addEventListener("click", clickField)
    }

    function clickField () {
        let check = false;
        for(let i=0; i<field.length; i++){
            if(field[i].classList.contains("active")) {
                check = true;
                break;
            }
        }

        if(!check){
            let textInField = this.innerHTML;
            if(textInField===hidden){
                correctAnswer(this);
            }else {
                failureAnswer(this);
            }
        }else {
            form.submit();
        }
    }

    function correctAnswer(tickField) {
        tickField.classList.add("success", "active")
        sendForm();
        check.value = "success";
    }

    function failureAnswer(tickField) {
        tickField.classList.add("failure", "active")
        sendForm();
        for(let i=0; i<field.length; i++){
            if(field[i].innerHTML === hidden){
                setInterval(() => {
                    field[i].classList.toggle("success");
                }, 250);
            }
        }
        check.value = "failure";
    }

    function sendForm(){
        setTimeout(function () {
            form.submit();
        }, 1500);
    }
})