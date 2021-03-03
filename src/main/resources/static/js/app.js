window.addEventListener("DOMContentLoaded", function () {
    let form = document.querySelector("form");
    let hidden = document.querySelector("#hidden").value;
    let check = document.querySelector("input[type=submit]");
    let know = document.querySelector("#know");
    let buttonHint = document.querySelector("#hint");
    let input = document.querySelector("input[name=word]");
    let knowAnswer = true;

    let words = hidden.split(", ");

    if (sessionStorage.getItem("value") === null) {
        sessionStorage.setItem("value", "3");
    }

    if (sessionStorage.getItem("correctAnswer") === null || parseInt(sessionStorage.getItem("correctAnswer")) >= 3) {
        sessionStorage.setItem("correctAnswer", 0);
    }

    buttonHint.innerHTML = "Hint (" + sessionStorage.getItem("value") + ")"

    if (buttonHint.innerHTML.charAt(6) === "0") {
        buttonHint.disabled = true;
        buttonHint.classList.add("disabled");
    }

    input.addEventListener("input", function(){
        for(let i=0; i < words.length; i++){
            if(words[i] != input.value && !(buttonHint.innerHTML.charAt(6) === "0")) {
                buttonHint.disabled = false;
                buttonHint.classList.remove("disabled");
            }
        }
    })


    know.addEventListener("click", function(){
        knowAnswer = false;
        input.value = hidden
        buttonHint.disabled = true;
        buttonHint.classList.add("disabled");
    })

    buttonHint.addEventListener("click", function () {
        words = hidden.split(", ");
        let length = input.value.length;
        let check = true;
        let noFit = true;

        if(!("(".includes(input.value))){
            checkInput();
        }

        for (let i = 0; i < words.length; i++) {
            if (words[i] === input.value) {
                check = false
                noFit = false;
                break;
            } else if (words[i].startsWith(input.value)) {
                input.value += words[i].charAt(length)
                noFit = false;
                break;
            } else if (words[i] != input.value) {
                let newWord = "";
                let charInput = input.value.split("");
                let charWord = words[i].split("");
                for (let j = 0; j < words[i].length; j++) {
                    if (charInput[j] === charWord[j]) {
                        newWord += charInput[j];
                    } else {
                        break;
                    }
                }
                if (newWord.length >= 1) {
                    noFit = false;
                    input.value = newWord + words[i].charAt(newWord.length)
                    break;
                }
            }
        }

        if (noFit) {
            input.value = words[0].charAt(0);
        }

        let hintQuantity = buttonHint.innerHTML.charAt(6)
        if (check) {
            hintQuantity = hintQuantity - 1;
            buttonHint.innerHTML = "Hint (" + hintQuantity + ")";
        }
        sessionStorage.setItem("value", hintQuantity);

        if (hintQuantity === 0 || !check) {
            buttonHint.disabled = true;
            buttonHint.classList.add("disabled");
        }
    })

    check.addEventListener("click", function (e) {
        e.preventDefault();

        words = hidden.split(", ");

        let check = false;
        let fit = false;
        let inputValue = input.value.split(",")

        for(let i=0; i < words.length; i++){
            for(let j=0; j < inputValue.length; j++){
                if(inputValue[j] === words[i]) {
                    fit = true;
                    break;
                }
            }
            if(fit){
                break;
            }
        }

        if(!fit){
            checkInput();
        }

        for (let i = 0; i < inputValue.length; i++) {
            inputValue[i] = inputValue[i].replaceAll(/\s/g, '');
            check = false
            for (let j = 0; j < words.length; j++) {
                words[j] = words[j].replaceAll(/\s/g, '');
                if (inputValue[i] === words[j]) {
                    check = true;
                    break;
                }
            }
            if (!check) {
                break;
            }
        }

        if (check) {
            if(knowAnswer){
                let correctAnswerValue = parseInt(sessionStorage.getItem("correctAnswer")) + 1;
                sessionStorage.setItem("correctAnswer", correctAnswerValue);
            }

            if (sessionStorage.getItem("correctAnswer") === "3") {
                if (sessionStorage.getItem("value") < 3) {
                    let value = parseInt(sessionStorage.getItem("value")) + 1;
                    sessionStorage.setItem("value", value);
                }
            }

            if (document.querySelector("span.error") == null) {
                addSpan("success", "Yes, that's the correct meaning")
            } else {
                let spanError = document.querySelector("span.error");
                form.removeChild(spanError);
                addSpan("success", "Yes, that's the correct meaning")
            }

            setTimeout(function () {
                form.submit();
            }, 1000);
        } else {
            if (document.querySelector("span.error") == null) {
                addSpan("error", "The given word is incorrect");
            }
        }
    })

    function addSpan(cssClass, sentence) {
        let word = document.querySelector(".english-word");
        let span = document.createElement("span");
        span.classList.add(cssClass)
        span.innerHTML = sentence
        form.insertBefore(span, word);
    }


    function checkInput(){
        for(let i=0; i < words.length; i++){
            let start = "";
            if(words[i].includes("(")) {
                let deleteTextInBracket = words[i].split("");
                for(let i=0; i< deleteTextInBracket.length; i++){
                    if(deleteTextInBracket[i].includes("(")){
                        start = i;
                        break;
                    }
                }
               words[i] = words[i].substring(0, start);
               words[i] = words[i].replaceAll(/\s/g, '');
            }
        }
    }
})