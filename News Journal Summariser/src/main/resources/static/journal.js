document.addEventListener("DOMContentLoaded", function () {
  fetchEntries();
  document
    .getElementById("addEntryForm")
    .addEventListener("submit", function (e) {
      e.preventDefault();
      addEntry();
    });
});

function fetchEntries() {
  fetch("/journal")
    .then((res) => res.json())
    .then((entries) => {
      const list = document.getElementById("entriesList");
      list.innerHTML = "";
      entries.forEach((entry) => {
        const li = document.createElement("li");
        li.innerHTML = `<b>${entry.title}</b>: ${entry.content} <button onclick="deleteEntry(${entry.id})">Delete</button> <button onclick="showUpdateForm(${entry.id}, '${entry.title}', '${entry.content}')">Edit</button>`;
        list.appendChild(li);
      });
    });
}

function addEntry() {
  const id = Number(document.getElementById("entryId").value);
  const title = document.getElementById("entryTitle").value;
  const content = document.getElementById("entryContent").value;
  fetch("/journal", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ id, title, content }),
  }).then(() => {
    fetchEntries();
    document.getElementById("addEntryForm").reset();
  });
}

function deleteEntry(id) {
  fetch(`/journal/delete/${id}`, { method: "DELETE" }).then(fetchEntries);
}

function showUpdateForm(id, title, content) {
  document.getElementById("entryId").value = id;
  document.getElementById("entryTitle").value = title;
  document.getElementById("entryContent").value = content;
  document.getElementById("addEntryForm").onsubmit = function (e) {
    e.preventDefault();
    updateEntry(id);
  };
}

function updateEntry(id) {
  const title = document.getElementById("entryTitle").value;
  const content = document.getElementById("entryContent").value;
  fetch(`/journal/update/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ id, title, content }),
  }).then(() => {
    fetchEntries();
    document.getElementById("addEntryForm").reset();
    document.getElementById("addEntryForm").onsubmit = function (e) {
      e.preventDefault();
      addEntry();
    };
  });
}
