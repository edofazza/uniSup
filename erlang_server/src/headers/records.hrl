-record(unisup_messages, {sender,
               receiver,
               text,
               timestamp
              }).

-record(unisup_users, {username,
                  password,
                  nodeName,
                  pid
                }).