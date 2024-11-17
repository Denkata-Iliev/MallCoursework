const dropArea = document.getElementById('drop-area');
const fileInput = document.getElementById('file-input');

function preventDefaults(e) {
    e.preventDefault();
    e.stopPropagation();
}

dropArea.addEventListener('dragover', preventDefaults);
dropArea.addEventListener('dragenter', preventDefaults);
dropArea.addEventListener('dragleave', preventDefaults);

dropArea.addEventListener('dragover', () => {
    dropArea.classList.add('drag-over');
});

dropArea.addEventListener('dragleave', () => {
    dropArea.classList.remove('drag-over');
});

dropArea.addEventListener('drop', handleDrop);
dropArea.onclick = e => {
    e.preventDefault();
    fileInput.click();
}

fileInput.onchange = e => {
    e.preventDefault();

    const files = e.currentTarget.files;
    if (files.length) {
        dropArea.innerText = files[0].name
    }
}

function handleDrop(e) {
    e.preventDefault();

    // list of dragged files
    const files = e.dataTransfer.files;

    // Checking if there are any files
    if (!files.length) {
        return;
    }

    // Assigning the files to the hidden input from the first step
    fileInput.files = files;

    dropArea.innerText = files[0].name;
    dropArea.classList.remove('drag-over');
}