const addFaveForm = document.getElementsByClassName('add-fave');
const csrfData = document.getElementById('csrf-data');
const notificationBox = document.getElementById('notification')

for (const el of addFaveForm) {
    el.onclick = async e => toggleFavorite(e, el);
}

async function toggleFavorite(event, element) {
    event.preventDefault();

    const isFavorite = element.classList.contains('fa-solid');
    const storeId = element.dataset.storeId;

    const csrfToken = csrfData.dataset.token;
    const csrfHeader = csrfData.dataset.header;
    try {
        const response = await fetch(`http://localhost:8080/users/favorites/${storeId}`, {
            method: isFavorite ? 'DELETE' : 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify({storeId: storeId})
        });

        if (response.status === 403) {
            alert("You can't add/remove a store to/from favorites if you're not a client!")
            return;
        }

        if (!response.ok) {
            const text = await response.text();
            console.error(text);
            return;
        }

        const text = await response.text();

        if (isFavorite) {
            // heart styles
            element.classList.remove('fa-solid', 'text-danger');
            element.classList.add('fa-regular');

            // pop-up message
            notificationBox.textContent = text;
            notificationBox.classList.add('bg-danger');
            notificationBox.classList.remove('bg-success');
        } else {
            element.classList.remove('fa-regular');
            element.classList.add('fa-solid', 'text-danger');

            notificationBox.textContent = text;
            notificationBox.classList.add('bg-success');
            notificationBox.classList.remove('bg-danger');
        }

        notificationBox.classList.add("show");

        setTimeout(() => {
            notificationBox.classList.remove("show");
        }, 2000);
    } catch (error) {
        console.error('Failed to add/remove favorite', error)
    }
}