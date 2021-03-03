window.addEventListener("DOMContentLoaded", function () {
    let inputPage = document.querySelector(".words__input");
    let form = document.querySelector("form")

    window.history.pushState('words', 'Title', '/?page='+ inputPage.value);

    inputPage.addEventListener("change", function(){
        form.submit();
    })
})