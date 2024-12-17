const editProfileBtn = document.getElementById('editProfileBtn');
const profileFormContainer = document.getElementById('profileFormContainer');
const cancelUpdateBtn = document.getElementById('cancelUpdateBtn');
const changePassBtn = document.getElementById('changePassBtn');
const changePassFormContainer = document.getElementById('changePassFormContainer');
const cancelChangePassBtn = document.getElementById('cancelChangePassBtn')

// show update form
editProfileBtn.onclick = () => {
    profileFormContainer.classList.remove('hidden-form');
    editProfileBtn.classList.add('d-none');
    changePassBtn.classList.add('d-none');
}

// hide update form
cancelUpdateBtn.onclick = () => {
    profileFormContainer.classList.add('hidden-form');
    editProfileBtn.classList.remove('d-none');
    changePassBtn.classList.remove('d-none');
}

// show change pass form
changePassBtn.onclick = () => {
    changePassFormContainer.classList.remove('hidden-form');
    editProfileBtn.classList.add('d-none');
    changePassBtn.classList.add('d-none');
}

// hide change pass form
cancelChangePassBtn.onclick = () => {
    changePassFormContainer.classList.add('hidden-form');
    editProfileBtn.classList.remove('d-none');
    changePassBtn.classList.remove('d-none');
}