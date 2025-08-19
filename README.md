# Milou

## Java Email Application

Milou is a simple **command-line email application** built with **Java**, **Hibernate (JPA)**, and **MySQL**.  
It lets users sign up, log in, and exchange emails with other users inside the system — similar to a mini in-app email service.

---

## 🚀 Features

- **User Accounts**

  - Sign up with name, email, and password.
  - Log in to your account.
  - Basic authentication flow.

- **Email Management**

  - ✉️ **Send emails** to one or multiple recipients.
  - 📥 **View emails**:
    - All received emails.
    - Unread emails.
    - Sent emails.
    - Read email by unique code.
  - 📌 **Reply** to emails.
  - 🔁 **Forward** emails to other recipients.
  - 📊 Each email is tracked with a unique code.
  - ✅ Emails have **read/unread status** per recipient.

- **Database-backed**
  - All users and emails are stored in a relational database (`MySQL`).
  - Uses **Hibernate ORM** to manage entities.

---

## 🏗️ Database Schema

Milou uses three tables:

1. **users**

   - Stores user accounts.
   - Fields: `id`, `name`, `email`, `password`.

2. **emails**

   - Stores email details (subject, body, sender, sent date).
   - Fields: `id`, `code`, `subject`, `body`, `sent_date`, `sender_id`.

3. **email_recipients**
   - Maps emails to their recipients, tracks if read/unread.
   - Fields: `id`, `email_id`, `recipient_id`, `is_read`.

Execute this file before running the application itself:
📂 Schema file: `sql/milou_schema.sql`

```bash
mysql -u root -p < sql/milou_schema.sql
```
