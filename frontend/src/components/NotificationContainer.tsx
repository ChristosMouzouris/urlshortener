import React, { type ReactNode, useState, useCallback } from 'react';
import Notification from './Notification';
import { NotificationEnum } from '../types/notificationEnum';
import { NotificationContext } from './NotificationContext';

export interface NotificationData {
  id: string;
  notificationText: string;
  notificationType: NotificationEnum;
}

interface ProviderProps {
  children: ReactNode;
}

const NotificationProvider: React.FC<ProviderProps> = ({ children }) => {
  const [notifications, setNotifications] = useState<NotificationData[]>([]);

  const addNotification = useCallback(
    (notificationText: string, notificationType: NotificationEnum) => {
      const id = crypto.randomUUID();
      setNotifications((prev) => [
        ...prev,
        { id, notificationText, notificationType },
      ]);

      setTimeout(() => removeNotification(id), 5000);
    },
    []
  );

  const removeNotification = (id: string) => {
    setNotifications((prev) => prev.filter((n) => n.id !== id));
  };

  return (
    <NotificationContext.Provider value={{ addNotification }}>
      {children}
      <div className="fixed top-1/8 right-1/2 transform translate-x-1/2 flex flex-col gap-4 z-50 md:top-4 md:right-4 md:transform-none md:translate-none">
        {notifications.map((n) => (
          <Notification
            key={n.id}
            id={n.id}
            notificationText={n.notificationText}
            notificationType={n.notificationType}
            onClose={removeNotification}
          />
        ))}
      </div>
    </NotificationContext.Provider>
  );
};

export default NotificationProvider;
