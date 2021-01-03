-record(unisup_messages, {sender,
               receiver,
               text,
               timestamp    % An incremented value or a real date
              }).

-record(unisup_users, {username,
                  password,
                  nodeName,
                  pid
                }).