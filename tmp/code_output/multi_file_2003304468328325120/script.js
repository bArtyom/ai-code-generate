document.addEventListener('DOMContentLoaded', () => {
  const taskInput = document.getElementById('taskInput');
  const addTaskBtn = document.getElementById('addTaskBtn');
  const taskList = document.getElementById('taskList');

  addTaskBtn.addEventListener('click', () => {
    const taskText = taskInput.value.trim();
    if (taskText) {
      const li = document.createElement('li');
      li.className = 'flex items-center';
      li.innerHTML = `<input type="checkbox" class="form-checkbox h-4 w-4 text-blue-500"> <span class="ml-2">${taskText}</span>`;
      taskList.appendChild(li);
      taskInput.value = '';
    }
  });
});