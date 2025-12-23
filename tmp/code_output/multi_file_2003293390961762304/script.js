document.addEventListener('DOMContentLoaded', () => {
    const taskInput = document.getElementById('taskInput');
    const addTaskBtn = document.getElementById('addTaskBtn');
    const taskList = document.getElementById('taskList');

    addTaskBtn.addEventListener('click', () => {
        const task = taskInput.value.trim();
        if (task) {
            const li = document.createElement('li');
            li.className = 'flex justify-between items-center';
            li.innerHTML = `<span>${task}</span> <button class="text-red-500 hover:text-red-700">删除</button>`;
            taskList.appendChild(li);
            taskInput.value = '';
        }
    });

    taskList.addEventListener('click', (e) => {
        if (e.target.classList.contains('text-red-500')) {
            e.target.parentElement.remove();
        }
    });
});